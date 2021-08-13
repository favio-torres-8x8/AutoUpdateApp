package com.faviotorres.autoupdateapp.polycom.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppInfoEntity(
    @Json(name = "version_code") val versionCode: Int = 0,
    @Json(name = "version_name") val versionName: String = "",
    val description: String = "",
)
