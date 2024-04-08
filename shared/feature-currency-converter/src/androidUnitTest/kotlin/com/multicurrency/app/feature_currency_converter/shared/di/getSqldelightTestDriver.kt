package com.multicurrency.app.feature_currency_converter.shared.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.multicurrency.app.feature_currency_converter.shared.MultiCurrencyDatabase

internal actual fun getSqliteTestDriver(): SqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    .also { MultiCurrencyDatabase.Schema.create(it) }
