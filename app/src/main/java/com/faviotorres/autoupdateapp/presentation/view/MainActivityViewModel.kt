package com.faviotorres.autoupdateapp.presentation.view

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faviotorres.autoupdateapp.BuildConfig
import com.faviotorres.autoupdateapp.polycom.domain.interactor.PolycomResult
import com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase.ApkUseCase
import com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase.AppInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appInfoUseCase: AppInfoUseCase,
    private val apkUseCase: ApkUseCase,
): ViewModel(), DefaultLifecycleObserver {

    private var newVersionCode: String = ""

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
        if (newVersionCode.isEmpty()) return

        viewModelScope.launch {
            when (val result = apkUseCase(newVersionCode)) {
                is PolycomResult.Failure -> Log.e("VIEW MODEL", "failure: ", result.exception)
                is PolycomResult.Success -> {
                    Log.d("VIEW MODEL", "success: ${result.data}")
                    _newApkPath.value = result.data.orEmpty()
                }
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
                    newVersionCode = result.data.versionCode.toString()
                    _newUpdate.value = result.data.versionCode > BuildConfig.VERSION_CODE
                }
            }
        }
    }
}