package com.faviotorres.autoupdateapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.lang.StringBuilder


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<ExtendedFloatingActionButton>(R.id.update_efab).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            startActivity(intent)
        }

        findViewById<TextView>(R.id.version_tv).text = StringBuilder()
            .append(BuildConfig.VERSION_NAME)
            .append(" (")
            .append(BuildConfig.VERSION_CODE)
            .append(")")
    }
}
