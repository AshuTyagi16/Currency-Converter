package com.multicurrency.app.core_network.shared.impl.di

import com.multicurrency.app.core_network.shared.impl.HttpEngineProvider
import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module

val httpEngineModule = module {
    single {
        HttpEngineProvider()
    }
    single {
        provideHttpEngine(httpEngineProvider = get())
    }
}

private fun provideHttpEngine(httpEngineProvider: HttpEngineProvider): HttpClientEngine {
    return httpEngineProvider.clientEngine()
}