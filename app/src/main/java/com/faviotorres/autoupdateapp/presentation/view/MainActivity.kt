package com.faviotorres.autoupdateapp.presentation.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.faviotorres.autoupdateapp.BuildConfig
import com.faviotorres.autoupdateapp.R
import com.faviotorres.autoupdateapp.databinding.ActivityMainBinding
import com.faviotorres.autoupdateapp.persistence.file.FileManager
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    companion object {
        private const val EXTERNAL_STORAGE_REQUEST = 100
    }

    private val viewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var fileManager: FileManager

    private lateinit var binding: ActivityMainBinding

    /* Lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        lifecycle.addObserver(viewModel)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_REQUEST)
        }

        observeNewApkPath()
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
    }


    /* Live Data Observers */

    private fun observeNewApkPath() {
        viewModel.newApkPath.observe(this) { path ->
            if (!path.endsWith("apk")) return@observe
            if (!fileManager.isNewUpdate(packageManager, path, BuildConfig.VERSION_CODE)) return@observe

            val intent = Intent(Intent.ACTION_VIEW)
            val file = File(path)
            try {
                val data: Uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file)
                intent.setDataAndType(data, "application/vnd.android.package-archive")
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(intent)
            } catch (e: Exception) {
                Log.e("ACTIVITY", "", e)
            }
        }
    }
}
