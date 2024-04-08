package com.multicurrency.app.feature_currency_converter.shared.data.local

import app.cash.sqldelight.db.SqlDriver
import com.multicurrency.app.core_network.shared.impl.di.networkModule
import com.multicurrency.app.feature_currency_converter.shared.di.featureCurrencyConverterModule
import com.multicurrency.app.feature_currency_converter.shared.di.testSqliteDriverModule
import com.multicurrency.app.feature_currency_converter.shared.util.DummyResponse
import com.multicurrency.app.feature_currency_converter.shared.util.TestBaseConstants
import com.multicurrency.app.feature_currency_converter.shared.util.fromDtoToCurrencyList
import com.multicurrency.app.feature_currency_converter.shared.util.fromDtoToRatesList
import com.multicurrency.app.feature_currency_converter.shared.util.toCurrencyDtoList
import com.multicurrency.app.feature_currency_converter.shared.util.toRateDtoList
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class CurrencyConverterLocalDataSourceTest : KoinTest {

    private val currencyConverterLocalDataSource: CurrencyConverterLocalDataSource by inject()

    private val sqlDriver: SqlDriver by inject()

    private val json: Json by inject()

    private val currencyList by lazy {
        json.parseToJsonElement(DummyResponse.currenciesSuccessResponse).toCurrencyDtoList()
            .fromDtoToCurrencyList()
    }

    private val ratesList by lazy {
        json.parseToJsonElement(DummyResponse.ratedSuccessResponse).toRateDtoList()
            .fromDtoToRatesList()
    }

    @BeforeTest
    fun before() = runTest {
        startKoin {
            modules(
                networkModule,
                featureCurrencyConverterModule,
                testSqliteDriverModule
            )
        }
    }

    @AfterTest
    fun after() {
        sqlDriver.close()
        stopKoin()
    }

    @Test
    fun `fetch all currencies success`() = runTest {
        currencyConverterLocalDataSource.insertAllCurrencies(currencyList)
        val currencies = currencyConverterLocalDataSource.fetchAllCurrencies().first()
        assertNotNull(currencies)
        assertEquals(currencies.isNotEmpty(), true)
        assertEquals(
            currencies.find { it.currencySymbol == TestBaseConstants.CURRENCY_INR }?.currencySymbol,
            TestBaseConstants.CURRENCY_INR
        )
    }

    @Test
    fun `delete all currencies success`() = runTest {
        currencyConverterLocalDataSource.deleteAllCurrencies()
        val currencies = currencyConverterLocalDataSource.fetchAllCurrencies().first()
        assertEquals(currencies.isEmpty(), true)
    }

    @Test
    fun `fetch all rates success`() = runTest {
        currencyConverterLocalDataSource.insertAllRates(ratesList)
        val rates = currencyConverterLocalDataSource.fetchAllRatesForValue().first()
        assertNotNull(rates)
        assertEquals(rates.isNotEmpty(), true)
        assertEquals(rates.find { it.currencySymbol == TestBaseConstants.CURRENCY_INR }?.currencySymbol, TestBaseConstants.CURRENCY_INR)
    }

    @Test
    fun `delete all rates success`() = runTest {
        currencyConverterLocalDataSource.deleteAllRates()
        val rates = currencyConverterLocalDataSource.fetchAllRatesForValue().first()
        assertEquals(rates.isEmpty(), true)
    }

    @Test
    fun `dollar value for currency success`() = runTest {
        currencyConverterLocalDataSource.insertAllRates(ratesList)
        val currency = currencyConverterLocalDataSource.fetchDollarRateForCurrency(TestBaseConstants.CURRENCY_INR)
        assertEquals(currency?.currencySymbol, TestBaseConstants.CURRENCY_INR)
        assertEquals(currency?.rate, TestBaseConstants.INR_VALUE_PER_DOLLAR)
    }

}