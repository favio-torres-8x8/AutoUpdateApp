package com.faviotorres.autoupdateapp.polycom.domain.interactor

sealed class PolycomResult<out T> {
    data class Success<T>(val data: T): PolycomResult<T>()
    data class Failure(val exception: Exception): PolycomResult<Nothing>()
}
