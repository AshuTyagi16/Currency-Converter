package com.multicurrency.app.core_network.shared.impl

import com.multicurrency.app.core_logger.shared.api.LoggerApi
import com.multicurrency.app.core_network.shared.CoreNetworkBuildKonfig
import com.multicurrency.app.core_network.shared.api.HttpClientApi
import com.multicurrency.app.core_network.shared.impl.util.NetworkApiConfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class HttpClientApiImpl(
    private val shouldEnableLogging: Boolean,
    private val json: Json,
    private val loggerApi: LoggerApi,
    private val httpClient: HttpClient
) : HttpClientApi {

    override fun getHttpClient(): HttpClient {
        return httpClient.config {
            expectSuccess = true

            //Default Request
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = CoreNetworkBuildKonfig.BASE_URL
                }
                contentType(ContentType.Application.Json)
            }

            //Timeout
            install(HttpTimeout) {
                requestTimeoutMillis = NetworkApiConfig.REQUEST_TIMEOUT_MILLIS
                socketTimeoutMillis = NetworkApiConfig.SOCKET_TIMEOUT_MILLIS
                connectTimeoutMillis = NetworkApiConfig.CONNECT_TIMEOUT_MILLIS
            }

            //Logging
            if (shouldEnableLogging) {
                install(Logging) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            loggerApi.logDWithTag(
                                tag = NetworkApiConfig.DEFAULT_LOG_TAG,
                                message = message
                            )
                        }
                    }
                    level = LogLevel.ALL
                }
            }

            //Response Observer
            if (shouldEnableLogging) {
                install(ResponseObserver) {
                    onResponse {
                        loggerApi.logDWithTag(
                            tag = NetworkApiConfig.DEFAULT_LOG_TAG,
                            message = it.bodyAsText()
                        )
                    }
                }
            }

            //Serialization
            install(ContentNegotiation) {
                json(json)
            }
        }
    }
}