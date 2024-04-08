package com.multicurrency.app.feature_currency_converter.shared.data.repository

import com.multicurrency.app.core_network.shared.api.data.model.NetworkResult
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Currency
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Rate
import kotlinx.coroutines.flow.Flow

interface CurrencyConverterRepository {

    suspend fun fetchAllCurrencies(): Flow<NetworkResult<List<Currency>>>

    suspend fun fetchAllRatesForValue(amount: Double, baseCurrency: String): Flow<NetworkResult<List<Rate>>>

}