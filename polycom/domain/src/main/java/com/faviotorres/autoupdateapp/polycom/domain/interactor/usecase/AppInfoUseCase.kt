package com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase

import com.faviotorres.autoupdateapp.polycom.domain.interactor.PolycomResult
import com.faviotorres.autoupdateapp.polycom.domain.model.AppInfo
import com.faviotorres.autoupdateapp.polycom.domain.repository.PolycomApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppInfoUseCase @Inject constructor (
    private val repository: PolycomApiRepository
) {

    suspend operator fun invoke(): PolycomResult<AppInfo> {
        return withContext(Dispatchers.IO) {
            try {
                val appInfo = repository.info()
                PolycomResult.Success(appInfo)
            } catch (e: Exception) {
                PolycomResult.Failure(e)
            }
        }
    }
}