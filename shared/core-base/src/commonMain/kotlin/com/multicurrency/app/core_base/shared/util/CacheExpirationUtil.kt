package com.multicurrency.app.core_base.shared.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.until

class CacheExpirationUtil {

    suspend fun isCacheExpired(lastWrittenTimestampInMillis: Long?): Boolean =
        withContext(Dispatchers.IO) {
            if (lastWrittenTimestampInMillis != null) {
                val currentTimestamp = Clock.System.now()
                val lastWrittenTimestamp =
                    Instant.fromEpochMilliseconds(lastWrittenTimestampInMillis)

                val secondsSinceLastWritten = lastWrittenTimestamp.until(
                    currentTimestamp,
                    DateTimeUnit.SECOND,
                    TimeZone.currentSystemDefault()
                )

                val maxCacheExpiryInSeconds = BaseConstants.CACHE_EXPIRE_TIME.inWholeSeconds
                secondsSinceLastWritten > maxCacheExpiryInSeconds
            } else {
                true
            }
        }
}