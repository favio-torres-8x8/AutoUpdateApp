package com.faviotorres.autoupdateapp.polycom.domain.repository

import com.faviotorres.autoupdateapp.polycom.domain.model.AppInfo

interface PolycomApiRepository {

    suspend fun info(): AppInfo
    suspend fun apk(versionCode: String): String?
}