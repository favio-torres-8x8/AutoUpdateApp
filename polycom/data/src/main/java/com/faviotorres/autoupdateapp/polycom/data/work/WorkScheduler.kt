package com.faviotorres.autoupdateapp.polycom.data.work

import androidx.lifecycle.LiveData
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler @Inject constructor(
    private val workManager: WorkManager
) {

    companion object {
        const val APK_UNIQUE_NAME = "apk_unique_name"
    }

    private val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    fun apk(versionCode: String, url: String): LiveData<WorkInfo> {
        val data = workDataOf(ApkWorker.NEW_VERSION_CODE_KEY to versionCode,
            ApkWorker.NEW_VERSION_URL_KEY to url)

        val request = OneTimeWorkRequestBuilder<ApkWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .setBackoffCriteria(BackoffPolicy.LINEAR, 15, TimeUnit.SECONDS)
            .build()

        workManager.enqueueUniqueWork(APK_UNIQUE_NAME, ExistingWorkPolicy.REPLACE, request)

        return workManager.getWorkInfoByIdLiveData(request.id)
    }

    fun cancelAll() {
        workManager.cancelAllWork()
    }
}