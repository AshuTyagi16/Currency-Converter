package com.multicurrency.app.feature_currency_converter.shared.util

object CurrencyConverterConstants {

    const val APP_ID = "app_id"
    const val HOMEPAGE_DATABASE_NAME = "MultiCurrencyDatabase"

    internal object Endpoints {
        const val FETCH_ALL_CURRENCIES = "api/currencies.json"
        const val FETCH_ALL_RATES = "api/latest.json"
    }

    internal object PreferenceKeys {
        const val CURRENCIES_LAST_WRITTEN_TIMESTAMP = "CURRENCIES_LAST_WRITTEN_TIMESTAMP"
        const val RATES_LAST_WRITTEN_TIMESTAMP = "RATES_LAST_WRITTEN_TIMESTAMP"
    }
}