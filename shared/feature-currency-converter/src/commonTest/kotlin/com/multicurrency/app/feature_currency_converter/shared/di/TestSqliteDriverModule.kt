package com.multicurrency.app.feature_currency_converter.shared.di

import app.cash.sqldelight.db.SqlDriver
import org.koin.dsl.module

val testSqliteDriverModule = module {
    single<SqlDriver> {
        getSqliteTestDriver()
    }
}