package com.faviotorres.autoupdateapp.model

sealed class MainEvent {
    object Update: MainEvent()
}
