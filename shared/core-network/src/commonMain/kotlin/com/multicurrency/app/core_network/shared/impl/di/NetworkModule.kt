package com.multicurrency.app.core_network.shared.impl.di

import com.multicurrency.app.core_logger.shared.CoreLoggerBuildKonfig
import com.multicurrency.app.core_network.shared.api.HttpClientApi
import com.multicurrency.app.core_network.shared.impl.HttpClientApiImpl
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<HttpClientApi> {
        HttpClientApiImpl(
            shouldEnableLogging = CoreLoggerBuildKonfig.IS_DEBUG,
            json = get(),
            loggerApi = get(),
            httpClient = HttpClient(engine = get())
        )
    }
    single {
        Json {
            ignoreUnknownKeys = true
            prettyPrint = false
            isLenient = true
            useAlternativeNames = true
            encodeDefaults = true
            explicitNulls = false
        }
    }
    single {
        provideHttpClient(httpClientApi = get())
    }
}

private fun provideHttpClient(httpClientApi: HttpClientApi): HttpClient {
    return httpClientApi.getHttpClient()
}