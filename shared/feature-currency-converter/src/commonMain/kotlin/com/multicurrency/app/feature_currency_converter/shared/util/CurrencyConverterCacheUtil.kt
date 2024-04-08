package com.multicurrency.app.feature_currency_converter.shared.util

import com.multicurrency.app.core_base.shared.util.CacheExpirationUtil
import com.multicurrency.app.core_preferences.shared.api.PreferenceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class CurrencyConverterCacheUtil(
    private val preferenceApi: PreferenceApi,
    private val cacheExpirationUtil: CacheExpirationUtil
) {

    suspend fun isCurrencyCacheExpired(): Boolean {
        val timestamp =
            preferenceApi.getLong(CurrencyConverterConstants.PreferenceKeys.CURRENCIES_LAST_WRITTEN_TIMESTAMP)
        return cacheExpirationUtil.isCacheExpired(timestamp)
    }

    suspend fun isRatesCacheExpired(): Boolean {
        val timestamp =
            preferenceApi.getLong(CurrencyConverterConstants.PreferenceKeys.RATES_LAST_WRITTEN_TIMESTAMP)
        return cacheExpirationUtil.isCacheExpired(timestamp)
    }

    suspend fun setCurrencyCacheLastWrittenTimestamp() = withContext(Dispatchers.IO) {
        preferenceApi.putLong(
            CurrencyConverterConstants.PreferenceKeys.CURRENCIES_LAST_WRITTEN_TIMESTAMP,
            Clock.System.now().toEpochMilliseconds()
        )
    }

    suspend fun setRatesCacheLastWrittenTimestamp() = withContext(Dispatchers.IO) {
        preferenceApi.putLong(
            CurrencyConverterConstants.PreferenceKeys.RATES_LAST_WRITTEN_TIMESTAMP,
            Clock.System.now().toEpochMilliseconds()
        )
    }
}