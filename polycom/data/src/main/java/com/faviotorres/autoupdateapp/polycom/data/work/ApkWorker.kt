package com.faviotorres.autoupdateapp.polycom.data.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.faviotorres.autoupdateapp.polycom.domain.interactor.PolycomResult
import com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase.ApkUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ApkWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val apkUseCase: ApkUseCase
): CoroutineWorker(context, workerParams) {

    companion object {
        const val NEW_VERSION_CODE_KEY = "new_version_code_key"
        const val NEW_VERSION_URL_KEY = "new_version_url_key"
        const val NEW_APK_PATH_KEY = "new_apk_path_key"
    }

    private val newVersionCode: String
        get() = inputData.getString(NEW_VERSION_CODE_KEY) ?: ""

    private val newVersionUrl: String
        get() = inputData.getString(NEW_VERSION_URL_KEY) ?: ""

    override suspend fun doWork(): Result {

        if (runAttemptCount >= 3) {
            Log.d("APK WORKER", "tried too many times")
            return Result.failure()
        }

        return when (val result = apkUseCase(newVersionCode, newVersionUrl)) {
            is PolycomResult.Failure -> {
                Log.e("APK WORKER", "failure: ", result.exception)
                Result.retry()
            }
            is PolycomResult.Success -> {
                Log.d("APK WORKER", "success: ${result.data}")
                val data = workDataOf(NEW_APK_PATH_KEY to result.data)
                Result.success(data)
            }
            else -> Result.failure()
        }
    }
}