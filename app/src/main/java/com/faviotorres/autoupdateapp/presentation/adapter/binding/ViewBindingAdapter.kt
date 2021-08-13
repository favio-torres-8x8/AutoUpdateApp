package com.faviotorres.autoupdateapp.presentation.adapter.binding

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun View.isVisible(visible: Boolean) {
    this.isVisible = visible
}