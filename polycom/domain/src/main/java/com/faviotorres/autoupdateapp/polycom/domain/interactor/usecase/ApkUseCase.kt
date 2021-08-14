package com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase

import com.faviotorres.autoupdateapp.polycom.domain.interactor.PolycomResult
import com.faviotorres.autoupdateapp.polycom.domain.repository.PolycomApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApkUseCase @Inject constructor(
    private val repository: PolycomApiRepository
) {

    suspend operator fun invoke(versionCode: String): PolycomResult<String?> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = repository.apk(versionCode)
                PolycomResult.Success(inputStream)
            } catch (e: Exception) {
                PolycomResult.Failure(e)
            }
        }
    }
}