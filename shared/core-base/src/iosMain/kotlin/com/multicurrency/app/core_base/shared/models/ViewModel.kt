package com.multicurrency.app.core_base.shared.models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

actual abstract class ViewModel actual constructor() {

    private val viewModelScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    actual val scope: CoroutineScope = viewModelScope

    protected actual open fun onCleared() {
        viewModelScope.cancel()
    }

    fun clear() {
        onCleared()
    }
}