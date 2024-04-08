package com.multicurrency.app.core_base.shared.models

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel as AndroidXViewModel

actual abstract class ViewModel actual constructor() : AndroidXViewModel() {
    actual override fun onCleared() {
        super.onCleared()
    }

    actual val scope: CoroutineScope = viewModelScope
}