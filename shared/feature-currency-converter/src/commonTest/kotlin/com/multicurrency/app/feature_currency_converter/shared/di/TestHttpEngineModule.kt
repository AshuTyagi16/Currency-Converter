package com.multicurrency.app.feature_currency_converter.shared.di

import com.multicurrency.app.feature_currency_converter.shared.util.DummyResponse
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import org.koin.dsl.module

fun testHttpEngineModule(isSuccess: Boolean) = module {
    single<HttpClientEngine> {
        MockEngine(
            MockEngineConfig().also {
                it.addHandler { request ->
                    respond(
                        content = if (isSuccess) DummyResponse.getResponseForEndpoint(request.url.encodedPath) else "",
                        status = if (isSuccess) HttpStatusCode.OK else HttpStatusCode.NotFound,
                        headers = headersOf(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString()
                        ),
                    )
                }
            }
        )
    }
}