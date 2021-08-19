package com.faviotorres.autoupdateapp.presentation.view

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import com.faviotorres.autoupdateapp.BuildConfig
import com.faviotorres.autoupdateapp.model.MainEvent
import com.faviotorres.autoupdateapp.model.SingleLiveEvent
import com.faviotorres.autoupdateapp.polycom.data.work.ApkWorker
import com.faviotorres.autoupdateapp.polycom.data.work.WorkScheduler
import com.faviotorres.autoupdateapp.polycom.domain.interactor.PolycomResult
import com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase.AppInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appInfoUseCase: AppInfoUseCase,
    private val workScheduler: WorkScheduler
): ViewModel(), DefaultLifecycleObserver {

    var newVersionCode: Long = 0
    private var newApkUrl: String = ""

    private val _event = SingleLiveEvent<MainEvent>()
    val event: MutableLiveData<MainEvent> = _event

    private val _appVersion = MutableLiveData<String>()
    val appVersion: LiveData<String> = _appVersion

    private val _newUpdate = MutableLiveData<Boolean>()
    val newUpdate: LiveData<Boolean> = _newUpdate

    private val _newApkPath = MutableLiveData<String>()
    val newApkPath: LiveData<String> = _newApkPath



    /* Lifecycle */

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        loadAppInfo()
        fetchAppInfo()
    }


    /* Click Listeners */

    fun update() {
        if (newVersionCode == 0L || newApkUrl.isEmpty()) return

        _event.value = MainEvent.Update
    }

    fun update(owner: LifecycleOwner) {
        workScheduler.cancelAll()
        workScheduler.apk(newVersionCode.toString(), newApkUrl).observe(owner) { workInfo ->
            Log.d("VIEW MODEL", "work info state: ${workInfo.state}")
            when (workInfo.state) {
                WorkInfo.State.SUCCEEDED -> {
                    val path = workInfo.outputData.getString(ApkWorker.NEW_APK_PATH_KEY) ?: ""
                    Log.d("VIEW MODEL", "success: $path")
                    _newApkPath.value = path
                }
                WorkInfo.State.FAILED -> {
                    Log.e("VIEW MODEL", "failure")
                }
                else -> { }
            }
        }
    }


    /* Network */

    private fun loadAppInfo() {
        _appVersion.value =
            StringBuilder("App version: ")
                .append(BuildConfig.VERSION_NAME)
                .append(" (")
                .append(BuildConfig.VERSION_CODE)
                .append(")").toString()
    }

    private fun fetchAppInfo() {
        viewModelScope.launch {
            when (val result = appInfoUseCase()) {
                is PolycomResult.Failure -> Log.e("VIEW MODEL", "failure: ", result.exception)
                is PolycomResult.Success -> {
                    Log.d("VIEW MODEL", "app info: ${result.data}")
                    with(result.data) {
                        newVersionCode = versionCode.toLong()
                        newApkUrl = url
                        _newUpdate.value = versionCode > BuildConfig.VERSION_CODE
                    }
                }
            }
        }
    }
}