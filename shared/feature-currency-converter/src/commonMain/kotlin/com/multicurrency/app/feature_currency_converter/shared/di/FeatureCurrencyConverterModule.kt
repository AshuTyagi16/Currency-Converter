package com.multicurrency.app.feature_currency_converter.shared.di

import com.multicurrency.app.feature_currency_converter.shared.MultiCurrencyDatabase
import com.multicurrency.app.feature_currency_converter.shared.data.local.CurrencyConverterLocalDataSource
import com.multicurrency.app.feature_currency_converter.shared.data.network.CurrencyConverterRemoteDataSource
import com.multicurrency.app.feature_currency_converter.shared.data.repository.CurrencyConverterRepository
import com.multicurrency.app.feature_currency_converter.shared.domain.repository.CurrencyConverterRepositoryImpl
import com.multicurrency.app.feature_currency_converter.shared.domain.use_case.FetchAllCurrenciesUseCase
import com.multicurrency.app.feature_currency_converter.shared.domain.use_case.FetchAllRatesUseCase
import com.multicurrency.app.feature_currency_converter.shared.util.CurrencyConverterCacheUtil
import org.koin.dsl.module

val featureCurrencyConverterModule = module {
    single {
        MultiCurrencyDatabase(
            driver = get()
        )
    }
    single {
        CurrencyConverterLocalDataSource(
            multiCurrencyDatabase = get()
        )
    }
    single {
        CurrencyConverterRemoteDataSource(
            httpClient = get()
        )
    }
    single {
        CurrencyConverterCacheUtil(
            preferenceApi = get(),
            cacheExpirationUtil = get()
        )
    }
    single<CurrencyConverterRepository> {
        CurrencyConverterRepositoryImpl(
            currencyConverterLocalDataSource = get(),
            currencyConverterRemoteDataSource = get(),
            currencyConverterCacheUtil = get()
        )
    }
    single {
        FetchAllCurrenciesUseCase(get<CurrencyConverterRepository>()::fetchAllCurrencies)
    }
    single {
        FetchAllRatesUseCase(get<CurrencyConverterRepository>()::fetchAllRatesForValue)
    }
}