package com.faviotorres.autoupdateapp.polycom.api

import com.faviotorres.autoupdateapp.polycom.api.model.AppInfoEntity
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface PolycomApi {

    @GET("favio-torres-8x8/AutoUpdateApp/main/api/version.json")
    suspend fun info(): AppInfoEntity

    @Streaming
    @GET
    suspend fun apk(@Url url: String): Response<ResponseBody>
}