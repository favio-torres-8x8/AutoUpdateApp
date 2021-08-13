package com.faviotorres.autoupdateapp.presentation

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.faviotorres.autoupdateapp.BuildConfig
import com.faviotorres.autoupdateapp.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.net.URLConnection

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()


    /* Lifecycle */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ExtendedFloatingActionButton>(R.id.update_efab).setOnClickListener {
            viewModel.hi()
        }

        findViewById<TextView>(R.id.version_tv).text = StringBuilder()
            .append(BuildConfig.VERSION_NAME)
            .append(" (")
            .append(BuildConfig.VERSION_CODE)
            .append(")")
    }

    /*private fun downloadFile(url: String, outputFile: File) {
        try {
            val u = URL(url)
            val conn: URLConnection = u.openConnection()
            val contentLength: Int = conn.contentLength
            val stream = DataInputStream(u.openStream())
            val buffer = ByteArray(contentLength)
            stream.readFully(buffer)
            stream.close()
            val fos = DataOutputStream(FileOutputStream(outputFile))
            fos.write(buffer)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.e(null, "file not found", e)
            return  // swallow a 404
        } catch (e: IOException) {
            Log.e(null, "io exception", e)
            return  // swallow a 404
        }
    }*/
}
