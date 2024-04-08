package com.multicurrency.app.core_network.shared.impl

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual class HttpEngineProvider {
    actual fun clientEngine(): HttpClientEngine = Darwin.create()

}