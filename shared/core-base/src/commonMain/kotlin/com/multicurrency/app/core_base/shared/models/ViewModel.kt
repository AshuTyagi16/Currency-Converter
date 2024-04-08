package com.multicurrency.app.core_base.shared.models

import kotlinx.coroutines.CoroutineScope

expect abstract class ViewModel() {
    val scope: CoroutineScope
    protected open fun onCleared()
}