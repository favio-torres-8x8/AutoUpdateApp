package com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase

import com.faviotorres.autoupdateapp.polycom.domain.interactor.PolycomResult
import com.faviotorres.autoupdateapp.polycom.domain.repository.PolycomApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApkUseCase @Inject constructor(
    private val repository: PolycomApiRepository
) {

    suspend operator fun invoke(versionCode: String, url: String): PolycomResult<String?> {
        return withContext(Dispatchers.IO) {
            try {
                repository.apk(versionCode, url)?.let { inputStream ->
                    PolycomResult.Success(inputStream)
                } ?: PolycomResult.Failure(Exception("Something wrong happened"))
            } catch (e: Exception) {
                PolycomResult.Failure(e)
            }
        }
    }
}