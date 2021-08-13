package com.faviotorres.autoupdateapp.polycom.data.repository

import com.faviotorres.autoupdateapp.polycom.api.PolycomApi
import com.faviotorres.autoupdateapp.polycom.data.extension.toDomain
import com.faviotorres.autoupdateapp.polycom.domain.model.AppInfo
import com.faviotorres.autoupdateapp.polycom.domain.repository.PolycomApiRepository
import javax.inject.Inject

class PolycomApiDataRepository @Inject constructor(
    private val api: PolycomApi
): PolycomApiRepository {

    override suspend fun info(): AppInfo {
        return api.info().toDomain()
    }
}