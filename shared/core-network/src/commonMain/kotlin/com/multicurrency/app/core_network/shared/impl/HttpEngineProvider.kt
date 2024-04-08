package com.multicurrency.app.core_network.shared.impl

import io.ktor.client.engine.HttpClientEngine

internal expect class HttpEngineProvider() {
    fun clientEngine(): HttpClientEngine
}