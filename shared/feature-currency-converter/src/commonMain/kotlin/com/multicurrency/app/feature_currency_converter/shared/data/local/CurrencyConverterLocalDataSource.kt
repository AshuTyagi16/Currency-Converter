package com.multicurrency.app.feature_currency_converter.shared.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.multicurrency.app.feature_currency_converter.shared.MultiCurrencyDatabase
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Currency
import com.multicurrency.app.feature_currency_converter.shared.domain.model.Rate
import com.multicurrency.app.featurecurrencyconverter.shared.RateEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

internal class CurrencyConverterLocalDataSource(
    multiCurrencyDatabase: MultiCurrencyDatabase
) {

    private val dbQuery = multiCurrencyDatabase.multiCurrencyDatabaseQueries

    suspend fun insertAllCurrencies(currencies: List<Currency>) = withContext(Dispatchers.IO) {
        dbQuery.transaction {
            dbQuery.deleteAllCurrencies()
            currencies.forEach {
                dbQuery.insertCurrency(
                    currencySymbol = it.currencySymbol,
                    currencyName = it.currencyName
                )
            }
        }
    }

    fun fetchAllCurrencies() =
        dbQuery.fetchAllCurrencies()
            .asFlow()
            .mapToList(Dispatchers.IO)

    suspend fun insertAllRates(rates: List<Rate>) = withContext(Dispatchers.IO) {
        dbQuery.transaction {
            dbQuery.deleteAllRates()
            rates.forEach {
                dbQuery.insertRate(
                    currencySymbol = it.currencySymbol,
                    rate = it.rate
                )
            }
        }
    }

    suspend fun fetchDollarRateForCurrency(currency: String): RateEntity? = withContext(Dispatchers.IO) {
        dbQuery.fetchDollarRateForCurrency(currency)
            .executeAsOneOrNull()
    }

    fun fetchAllRatesForValue() =
        dbQuery.fetchAllRates()
            .asFlow()
            .mapToList(Dispatchers.IO)

    suspend fun deleteAllRates()= withContext(Dispatchers.IO){
        dbQuery.transaction {
            dbQuery.deleteAllRates()
        }
    }

    suspend fun deleteAllCurrencies()= withContext(Dispatchers.IO){
        dbQuery.transaction {
            dbQuery.deleteAllCurrencies()
        }
    }
}

