package com.multicurrency.app.core_network.shared.api

import io.ktor.client.HttpClient

interface HttpClientApi {

    fun getHttpClient(): HttpClient

}