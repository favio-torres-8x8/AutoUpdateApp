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
import com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase.AppInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appInfoUseCase: AppInfoUseCase
): ViewModel(), DefaultLifecycleObserver {

    private val _appVersion = MutableLiveData<String>()
    val appVersion: LiveData<String> get() = _appVersion

    private val _newUpdate = MutableLiveData<Boolean>()
    val newUpdate: LiveData<Boolean> get() = _newUpdate

    /* Lifecycle */

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        loadAppInfo()
        fetchAppInfo()
    }


    /* Click Listeners */

    fun update() {
        Log.d("VIEW MODEL", "updating!!!!!!")
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
                    _newUpdate.value = result.data.versionCode > BuildConfig.VERSION_CODE
                }
            }
        }
    }
}