package com.faviotorres.autoupdateapp.persistence.file

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject


class FileManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getCacheSubDirectory(name: String): File {
        val cacheDir = File(context.cacheDir.toString() + "/" + name)
        if (!cacheDir.exists()) {
            val cacheDirCreated = cacheDir.mkdir()
            if (!cacheDirCreated) {
                Log.w("FILE MANAGER", "Cannot create /cache folder!")
            }
        }
        return cacheDir
    }

    fun writeToFile(inputStream: InputStream, file: File?) {
        Log.d("FILE MANAGER", "writing to file")
        inputStream.use { stream ->
            FileOutputStream(file).use { fileOutputStream ->
                val readBuffer = ByteArray(4096)
                write(stream, fileOutputStream, readBuffer)
            }
        }
    }

    @Throws(IOException::class)
    private fun write(inputStream: InputStream, fileOutputStream: FileOutputStream, readBuffer: ByteArray) {
        while (true) {
            val bytesRead = inputStream.read(readBuffer)
            if (bytesRead == -1) {
                Log.d("FILE MANAGER", "flushing")
                fileOutputStream.flush()
                break
            }
            fileOutputStream.write(readBuffer, 0, bytesRead)
            Log.d("FILE MANAGER", "writing output - read: $bytesRead")
        }
    }

    @Suppress("DEPRECATION")
    fun isNewUpdate(packageManager: PackageManager, path: String, newVersionCodeFromJson: Long): Boolean {
        return packageManager.getPackageArchiveInfo(path, 0)?.let { info ->
            val newVersionCodeFromApk = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info.longVersionCode
            } else {
                info.versionCode.toLong()
            }
            Log.d("FILE MANAGER", "are versions the same? json: $newVersionCodeFromJson - apk: $newVersionCodeFromApk")
            Log.d("FILE MANAGER", "installing apk with version code: $newVersionCodeFromApk and version name: ${info.versionName}")
            newVersionCodeFromJson == newVersionCodeFromApk
        } ?: false
    }
}
