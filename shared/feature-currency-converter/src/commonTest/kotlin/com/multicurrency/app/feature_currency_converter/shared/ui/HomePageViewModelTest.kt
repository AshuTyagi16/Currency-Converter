package com.multicurrency.app.feature_currency_converter.shared.ui

import app.cash.turbine.test
import com.multicurrency.app.core_logger.shared.impl.di.loggerModule
import com.multicurrency.app.core_network.shared.impl.di.networkModule
import com.multicurrency.app.core_preferences.shared.impl.di.testPreferencesModule
import com.multicurrency.app.feature_currency_converter.shared.di.featureCurrencyConverterModule
import com.multicurrency.app.feature_currency_converter.shared.di.testHttpEngineModule
import com.multicurrency.app.feature_currency_converter.shared.di.testSqliteDriverModule
import com.multicurrency.app.feature_currency_converter.shared.domain.use_case.FetchAllCurrenciesUseCase
import com.multicurrency.app.feature_currency_converter.shared.domain.use_case.FetchAllRatesUseCase
import com.multicurrency.app.feature_currency_converter.shared.ui.HomePageContract
import com.multicurrency.app.feature_currency_converter.shared.ui.HomePageViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
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

@OptIn(ExperimentalCoroutinesApi::class)
class HomePageViewModelTest : KoinTest {

    private val fetchAllCurrenciesUseCase: FetchAllCurrenciesUseCase by inject()

    private val fetchAllRatesUseCase: FetchAllRatesUseCase by inject()

    private val defaultHttpEngineModule = testHttpEngineModule(isSuccess = true)

    private val viewModel by lazy {
        HomePageViewModel(
            fetchAllCurrenciesUseCase = fetchAllCurrenciesUseCase,
            fetchAllRatesUseCase = fetchAllRatesUseCase,
        )
    }

    @BeforeTest
    fun before() = runTest {
        startKoin {
            modules(
                networkModule,
                loggerModule,
                featureCurrencyConverterModule,
                testSqliteDriverModule,
                testPreferencesModule,
                defaultHttpEngineModule
            )
        }
        Dispatchers.setMain(StandardTestDispatcher())
        viewModel.init()
    }

    @AfterTest
    fun after() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state test success`() = runTest {
        viewModel.uiState.test {
            assertEquals(
                awaitItem(),
                HomePageContract.State(
                    isLoadingCurrencies = false,
                    isLoadingRates = false,
                    amount = "0",
                    baseCurrency = "USD",
                    currencies = emptyList(),
                    rates = emptyList()
                )
            )
        }
    }

    @Test
    fun `fetch currency list success`() = runTest {
        advanceTimeBy(1000)
        viewModel.setEvent(HomePageContract.Event.OnFetchAllCurrenciesEvent)
        viewModel.uiState.test {
            awaitItem()
            assertEquals(awaitItem().isLoadingCurrencies, true)
            assertEquals(awaitItem().currencies.isNotEmpty(), true)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetch rates list success`() = runTest {
        advanceTimeBy(1000)
        viewModel.setEvent(HomePageContract.Event.OnFetchAllRatesEvent)
        viewModel.uiState.test {
            awaitItem()
            assertEquals(awaitItem().isLoadingRates, true)
            assertEquals(awaitItem().amount.isNotEmpty(), true)
            cancelAndConsumeRemainingEvents()
        }
    }
}