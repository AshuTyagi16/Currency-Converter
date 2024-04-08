package com.multicurrency.app.feature_currency_converter.shared.data.network

import com.multicurrency.app.core_logger.shared.impl.di.loggerModule
import com.multicurrency.app.core_network.shared.api.data.model.NetworkResult
import com.multicurrency.app.core_network.shared.impl.di.networkModule
import com.multicurrency.app.core_preferences.shared.impl.di.preferencesModule
import com.multicurrency.app.feature_currency_converter.shared.di.featureCurrencyConverterModule
import com.multicurrency.app.feature_currency_converter.shared.di.testHttpEngineModule
import com.multicurrency.app.feature_currency_converter.shared.util.TestBaseConstants
import kotlinx.coroutines.test.runTest
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.context.unloadKoinModules
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CurrencyConverterRemoteDataSourceTest : KoinTest {

    private val currencyConverterRemoteDataSource: CurrencyConverterRemoteDataSource by inject()

    @BeforeTest
    fun before() {
        startKoin {
            modules(
                networkModule,
                loggerModule,
                preferencesModule,
                featureCurrencyConverterModule
            )
        }
    }

    @AfterTest
    fun after() {
        stopKoin()
    }

    @Test
    fun fetchCurrenciesSuccess() = runTest {
        val module = testHttpEngineModule(isSuccess = true)

        loadKoinModules(module)
        val result = currencyConverterRemoteDataSource.fetchAllCurrencies()

        assertEquals(result.status, NetworkResult.Status.SUCCESS)

        assertEquals(result.data.isNullOrEmpty().not(), true)

        assertEquals(result.data?.find { it.currencySymbol == TestBaseConstants.CURRENCY_INR }?.currencySymbol, TestBaseConstants.CURRENCY_INR)

        unloadKoinModules(module)
    }

    @Test
    fun fetchCurrenciesFailure() = runTest {
        val module = testHttpEngineModule(isSuccess = false)
        loadKoinModules(module)
        val result = currencyConverterRemoteDataSource.fetchAllCurrencies()

        assertEquals(result.status, NetworkResult.Status.ERROR)

        unloadKoinModules(module)
    }

    @Test
    fun fetchRatesSuccess() = runTest {
        val module = testHttpEngineModule(isSuccess = true)
        loadKoinModules(module)
        val result = currencyConverterRemoteDataSource.fetchAllRates()

        assertEquals(result.status, NetworkResult.Status.SUCCESS)

        assertEquals(result.data.isNullOrEmpty().not(), true)

        assertEquals(result.data?.find { it.currencySymbol == TestBaseConstants.CURRENCY_INR }?.rate, TestBaseConstants.INR_VALUE_PER_DOLLAR)

        unloadKoinModules(module)
    }

    @Test
    fun fetchRatedFailure() = runTest {
        val module = testHttpEngineModule(isSuccess = false)
        loadKoinModules(module)
        val result = currencyConverterRemoteDataSource.fetchAllRates()

        assertEquals(result.status, NetworkResult.Status.ERROR)

        unloadKoinModules(module)
    }
}