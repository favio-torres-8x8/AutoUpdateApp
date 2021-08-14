package com.faviotorres.autoupdateapp.polycom.data.repository

import android.util.Log
import com.faviotorres.autoupdateapp.persistence.file.FileManager
import com.faviotorres.autoupdateapp.polycom.api.PolycomApi
import com.faviotorres.autoupdateapp.polycom.data.extension.toDomain
import com.faviotorres.autoupdateapp.polycom.domain.model.AppInfo
import com.faviotorres.autoupdateapp.polycom.domain.repository.PolycomApiRepository
import java.io.File
import javax.inject.Inject

class PolycomApiDataRepository @Inject constructor(
    private val api: PolycomApi,
    private val fileManager: FileManager
): PolycomApiRepository {

    override suspend fun info(): AppInfo {
        return api.info().toDomain()
    }

    override suspend fun apk(versionCode: String): String? {
        val url = "https://github.com/favio-torres-8x8/AutoUpdateApp/blob/main/apks/app-debug-$versionCode.apk?raw=true"
        val response = api.apk(url)
        val subDirectory = fileManager.getCacheSubDirectory("apks")
        val apk = File(subDirectory, "apk-release-$versionCode.apk")
        Log.d("VIEW MODEL", "saving at: $apk")
        response.body()?.let {
            fileManager.writeToFile(it.byteStream(), apk)
        }
        return if (response.isSuccessful) apk.absolutePath else null
    }
}