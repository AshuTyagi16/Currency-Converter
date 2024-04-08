package com.multicurrency.app.feature_currency_converter.shared.domain.repository

import com.multicurrency.app.core_base.shared.util.BaseConstants
import com.multicurrency.app.core_base.shared.util.roundUp
import com.multicurrency.app.core_network.shared.api.data.model.NetworkResult
import com.multicurrency.app.core_network.shared.impl.util.isSuccess
import com.multicurrency.app.core_network.shared.impl.util.mapFromDTO
import com.multicurrency.app.feature_currency_converter.shared.data.local.CurrencyConverterLocalDataSource
import com.multicurrency.app.feature_currency_converter.shared.data.network.CurrencyConverterRemoteDataSource
import com.multicurrency.app.feature_currency_converter.shared.data.repository.CurrencyConverterRepository
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Currency
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Rate
import com.multicurrency.app.feature_currency_converter.shared.util.CurrencyConverterCacheUtil
import com.multicurrency.app.feature_currency_converter.shared.util.fromDtoToCurrencyList
import com.multicurrency.app.feature_currency_converter.shared.util.fromDtoToRatesList
import com.multicurrency.app.feature_currency_converter.shared.util.fromEntityToCurrencyList
import com.multicurrency.app.feature_currency_converter.shared.util.fromEntityToRatesList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse

internal class CurrencyConverterRepositoryImpl(
    private val currencyConverterLocalDataSource: CurrencyConverterLocalDataSource,
    private val currencyConverterRemoteDataSource: CurrencyConverterRemoteDataSource,
    private val currencyConverterCacheUtil: CurrencyConverterCacheUtil
) : CurrencyConverterRepository {

    companion object {
        private const val FETCH_ALL_CURRENCIES = "FETCH_ALL_CURRENCIES"
        private const val FETCH_ALL_RATES = "FETCH_ALL_RATES"
    }

    private val currencyStore =
        StoreBuilder.from<String, NetworkResult<List<Currency>>, NetworkResult<List<Currency>>>(
            fetcher = Fetcher.of { key ->
                val result = currencyConverterRemoteDataSource.fetchAllCurrencies()
                    .mapFromDTO { currencies ->
                        currencies?.fromDtoToCurrencyList().orEmpty()
                    }
                if (result.isSuccess()) {
                    result
                } else {
                    throw Exception(result.errorMessage.orEmpty())
                }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key ->
                    currencyConverterLocalDataSource.fetchAllCurrencies()
                        .map {
                            NetworkResult.success(it.fromEntityToCurrencyList())
                        }
                },
                writer = { key, input ->
                    if (input.isSuccess()) {
                        currencyConverterCacheUtil.setCurrencyCacheLastWrittenTimestamp()
                        currencyConverterLocalDataSource.insertAllCurrencies(
                            currencies = input.data.orEmpty()
                        )
                    }
                }
            )
        )
            .cachePolicy(
                MemoryPolicy.builder<String, NetworkResult<List<Currency>>>()
                    .setExpireAfterWrite(BaseConstants.CACHE_EXPIRE_TIME)
                    .build()
            )
            .build()

    private val ratesStore =
        StoreBuilder.from<String, NetworkResult<List<Rate>>, NetworkResult<List<Rate>>>(
            fetcher = Fetcher.of { key ->
                val result = currencyConverterRemoteDataSource.fetchAllRates()
                    .mapFromDTO { rates ->
                        rates?.fromDtoToRatesList().orEmpty()
                    }
                if (result.isSuccess()) {
                    result
                } else {
                    throw Exception(result.errorMessage.orEmpty())
                }
            },
            sourceOfTruth = SourceOfTruth.of(
                reader = { key ->
                    currencyConverterLocalDataSource.fetchAllRatesForValue()
                        .map {
                            NetworkResult.success(it.fromEntityToRatesList())
                        }
                },
                writer = { key, input ->
                    if (input.isSuccess()) {
                        currencyConverterCacheUtil.setRatesCacheLastWrittenTimestamp()
                        currencyConverterLocalDataSource.insertAllRates(
                            rates = input.data.orEmpty()
                        )
                    }
                }
            )
        )
            .cachePolicy(
                MemoryPolicy.builder<String, NetworkResult<List<Rate>>>()
                    .setExpireAfterWrite(BaseConstants.CACHE_EXPIRE_TIME)
                    .build()
            )
            .build()

    override suspend fun fetchAllCurrencies(): Flow<NetworkResult<List<Currency>>> {
        val isCacheExpired = currencyConverterCacheUtil.isCurrencyCacheExpired()
        return currencyStore.stream(
            StoreReadRequest.cached(
                key = FETCH_ALL_CURRENCIES,
                refresh = isCacheExpired
            )
        )
            .flowOn(Dispatchers.IO)
            .map {
                when (it) {
                    is StoreReadResponse.Data -> {
                        it.value
                    }

                    is StoreReadResponse.Error.Exception -> {
                        NetworkResult.error(errorMessage = it.errorMessageOrNull().orEmpty())
                    }

                    is StoreReadResponse.Error.Message -> {
                        NetworkResult.error(errorMessage = it.errorMessageOrNull().orEmpty())
                    }

                    is StoreReadResponse.Loading -> {
                        NetworkResult.loading()
                    }

                    is StoreReadResponse.NoNewData -> {
                        NetworkResult.error(errorMessage = it.errorMessageOrNull().orEmpty())
                    }
                }
            }
    }

    override suspend fun fetchAllRatesForValue(
        amount: Double,
        baseCurrency: String
    ): Flow<NetworkResult<List<Rate>>> {
        val isCacheExpired = currencyConverterCacheUtil.isRatesCacheExpired()
        return ratesStore.stream(
            StoreReadRequest.cached(
                key = FETCH_ALL_RATES,
                refresh = isCacheExpired
            )
        )
            .map {
                when (it) {
                    is StoreReadResponse.Data -> {
                        val dollarRateForCurrency =
                            currencyConverterLocalDataSource.fetchDollarRateForCurrency(baseCurrency)?.rate ?: 1.0
                        it.value.mapFromDTO {
                            it?.map {
                                it.copy(
                                    rate = (it.rate * (amount / dollarRateForCurrency)).roundUp(4)
                                )
                            }.orEmpty()
                        }
                    }

                    is StoreReadResponse.Error.Exception -> {
                        NetworkResult.error(errorMessage = it.errorMessageOrNull().orEmpty())
                    }

                    is StoreReadResponse.Error.Message -> {
                        NetworkResult.error(errorMessage = it.errorMessageOrNull().orEmpty())
                    }

                    is StoreReadResponse.Loading -> {
                        NetworkResult.loading()
                    }

                    is StoreReadResponse.NoNewData -> {
                        NetworkResult.error(errorMessage = it.errorMessageOrNull().orEmpty())
                    }
                }
            }
            .flowOn(Dispatchers.IO)
    }
}