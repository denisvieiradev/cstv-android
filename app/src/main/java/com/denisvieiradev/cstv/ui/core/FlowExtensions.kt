package com.denisvieiradev.cstv.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

private const val STATE_STOP_TIMEOUT_MILLIS = 5_000L

fun <T> MutableStateFlow<T>.stateInWhileSubscribed(
    viewModel: ViewModel,
    initialValue: T
): StateFlow<T> = stateIn(
    scope = viewModel.viewModelScope,
    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = STATE_STOP_TIMEOUT_MILLIS),
    initialValue = initialValue
)
