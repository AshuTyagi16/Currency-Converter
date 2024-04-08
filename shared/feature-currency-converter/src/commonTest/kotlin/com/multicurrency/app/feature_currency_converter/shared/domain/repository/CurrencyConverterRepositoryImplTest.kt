package com.multicurrency.app.feature_currency_converter.shared.domain.repository

import app.cash.turbine.test
import com.multicurrency.app.core_logger.shared.impl.di.loggerModule
import com.multicurrency.app.core_network.shared.api.data.model.NetworkResult
import com.multicurrency.app.core_network.shared.impl.di.networkModule
import com.multicurrency.app.core_preferences.shared.impl.di.testPreferencesModule
import com.multicurrency.app.feature_currency_converter.shared.data.repository.CurrencyConverterRepository
import com.multicurrency.app.feature_currency_converter.shared.di.featureCurrencyConverterModule
import com.multicurrency.app.feature_currency_converter.shared.di.testHttpEngineModule
import com.multicurrency.app.feature_currency_converter.shared.di.testSqliteDriverModule
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

class CurrencyConverterRepositoryImplTest : KoinTest {

    private val currencyConverterRepository: CurrencyConverterRepository by inject()

    @BeforeTest
    fun before() = runTest {
        startKoin {
            modules(
                networkModule,
                loggerModule,
                featureCurrencyConverterModule,
                testSqliteDriverModule,
                testPreferencesModule
            )
        }
    }

    @AfterTest
    fun after() {
        stopKoin()
    }

    @Test
    fun `fetch all currencies success`() = runTest {
        val module = testHttpEngineModule(isSuccess = true)
        loadKoinModules(module)
        currencyConverterRepository.fetchAllCurrencies().test {

            assertEquals(
                awaitItem().data.isNullOrEmpty(),
                true
            ) // Initial Data From In-Memory Cache Which will be null

            assertEquals(
                awaitItem().status,
                NetworkResult.Status.LOADING
            ) // Invokes Fetcher & Emits Loading

            val finalItem = awaitItem()

            assertEquals(finalItem.data.isNullOrEmpty().not(), true) // Fresh Data From APi
            assertEquals(
                finalItem.data?.find { it.currencySymbol == TestBaseConstants.CURRENCY_INR }?.currencySymbol,
                TestBaseConstants.CURRENCY_INR
            ) // Fresh Data From APi

            cancelAndIgnoreRemainingEvents()
        }
        unloadKoinModules(module)
    }

    @Test
    fun `fetch all currencies failure`() = runTest {
        val module = testHttpEngineModule(isSuccess = false)
        loadKoinModules(module)
        currencyConverterRepository.fetchAllCurrencies().test {
            assertEquals(
                awaitItem().data.isNullOrEmpty(),
                true
            ) // Initial Data From In-Memory Cache Which will be null

            assertEquals(
                awaitItem().status,
                NetworkResult.Status.LOADING
            ) // Invokes Fetcher & Emits Loading

            assertEquals(
                awaitItem().data.isNullOrEmpty(),
                true
            ) // Fresh Data From API which will be null in this case

            expectNoEvents()
        }
        unloadKoinModules(module)
    }


    @Test
    fun `fetch all rates for value and currency success`() = runTest {
        val module = testHttpEngineModule(isSuccess = true)
        loadKoinModules(module)
        currencyConverterRepository.fetchAllRatesForValue(
            amount = 1.0,
            baseCurrency = "USD"
        ).test {

            assertEquals(
                awaitItem().data.isNullOrEmpty(),
                true
            ) // Initial Data From In-Memory Cache Which will be null

            assertEquals(
                awaitItem().status,
                NetworkResult.Status.LOADING
            ) // Invokes Fetcher & Emits Loading

            val finalItem = awaitItem()

            assertEquals(finalItem.data.isNullOrEmpty().not(), true) // Fresh Data From APi

            assertEquals(
                finalItem.data?.find { it.currencySymbol == TestBaseConstants.CURRENCY_INR }?.rate,
                TestBaseConstants.INR_VALUE_PER_DOLLAR
            )

            expectNoEvents()
        }
        unloadKoinModules(module)
    }

    @Test
    fun `fetch all rates for value and currency failure`() = runTest {
        val module = testHttpEngineModule(isSuccess = false)
        loadKoinModules(module)
        currencyConverterRepository.fetchAllRatesForValue(
            amount = 1.0,
            baseCurrency = "USD"
        ).test {
            assertEquals(
                awaitItem().data.isNullOrEmpty(),
                true
            ) // Initial Data From In-Memory Cache Which will be null

            assertEquals(
                awaitItem().status,
                NetworkResult.Status.LOADING
            ) // Invokes Fetcher & Emits Loading

            assertEquals(
                awaitItem().data.isNullOrEmpty(),
                true
            ) // Fresh Data From API which will be null in this case

            expectNoEvents()
        }
        unloadKoinModules(module)
    }

}