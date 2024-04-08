package com.multicurrency.app.feature_currency_converter.shared.data.network

import com.multicurrency.app.core_network.shared.CoreNetworkBuildKonfig
import com.multicurrency.app.core_network.shared.api.data.base.BaseDataSource
import com.multicurrency.app.core_network.shared.api.data.model.NetworkResult
import com.multicurrency.app.core_network.shared.impl.util.mapFromJsonElement
import com.multicurrency.app.feature_currency_converter.shared.data.dto.CurrencyDto
import com.multicurrency.app.feature_currency_converter.shared.data.dto.RateDto
import com.multicurrency.app.feature_currency_converter.shared.util.CurrencyConverterConstants
import com.multicurrency.app.feature_currency_converter.shared.util.CurrencyConverterConstants.Endpoints
import com.multicurrency.app.feature_currency_converter.shared.util.toCurrencyDtoList
import com.multicurrency.app.feature_currency_converter.shared.util.toRateDtoList
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kotlinx.serialization.json.JsonElement

internal class CurrencyConverterRemoteDataSource(
    private val httpClient: HttpClient
) : BaseDataSource() {

    suspend fun fetchAllCurrencies(): NetworkResult<List<CurrencyDto>> = getResult<JsonElement> {
        httpClient.get {
            url(Endpoints.FETCH_ALL_CURRENCIES)
            parameter(CurrencyConverterConstants.APP_ID, CoreNetworkBuildKonfig.APP_ID)
        }
    }.mapFromJsonElement {
        it.toCurrencyDtoList()
    }

    suspend fun fetchAllRates(): NetworkResult<List<RateDto>> = getResult<JsonElement> {
        httpClient.get {
            url(Endpoints.FETCH_ALL_RATES)
            parameter(CurrencyConverterConstants.APP_ID, CoreNetworkBuildKonfig.APP_ID)
        }
    }.mapFromJsonElement {
        it.toRateDtoList()
    }
}