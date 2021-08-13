package com.faviotorres.autoupdateapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faviotorres.autoupdateapp.polycom.domain.interactor.PolycomResult
import com.faviotorres.autoupdateapp.polycom.domain.interactor.usecase.AppInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val appInfoUseCase: AppInfoUseCase
): ViewModel() {

    fun hi() {
        viewModelScope.launch {
            when (val result = appInfoUseCase()) {
                is PolycomResult.Failure -> Log.e("VIEW MODEL", "failure: ", result.exception)
                is PolycomResult.Success -> Log.d("VIEW MODEL", "success: ${result.data}")
            }
        }
    }
}