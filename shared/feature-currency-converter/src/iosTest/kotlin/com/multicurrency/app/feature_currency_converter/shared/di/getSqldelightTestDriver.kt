package com.multicurrency.app.feature_currency_converter.shared.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.inMemoryDriver
import com.multicurrency.app.feature_currency_converter.shared.MultiCurrencyDatabase

internal actual fun getSqliteTestDriver(): SqlDriver = inMemoryDriver(MultiCurrencyDatabase.Schema)