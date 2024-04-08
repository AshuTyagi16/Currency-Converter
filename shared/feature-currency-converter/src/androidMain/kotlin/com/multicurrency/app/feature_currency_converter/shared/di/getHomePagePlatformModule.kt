package com.multicurrency.app.feature_currency_converter.shared.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.multicurrency.app.feature_currency_converter.shared.MultiCurrencyDatabase
import com.multicurrency.app.feature_currency_converter.shared.ui.HomePageViewModel
import com.multicurrency.app.feature_currency_converter.shared.util.CurrencyConverterConstants
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun getHomePagePlatformModule(): Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(
            MultiCurrencyDatabase.Schema,
            get(),
            CurrencyConverterConstants.HOMEPAGE_DATABASE_NAME
        )
    }
    viewModel { HomePageViewModel(get(), get()) }
}