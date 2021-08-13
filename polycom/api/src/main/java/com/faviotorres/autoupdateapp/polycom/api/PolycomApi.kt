package com.faviotorres.autoupdateapp.polycom.api

import com.faviotorres.autoupdateapp.polycom.api.model.AppInfoEntity
import retrofit2.http.GET

interface PolycomApi {

    @GET("favio-torres-8x8/AutoUpdateApp/blob/main/api/version.json")
    suspend fun info(): AppInfoEntity
}