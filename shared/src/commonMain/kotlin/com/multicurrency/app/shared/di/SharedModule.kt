package com.multicurrency.app.shared.di

import com.multicurrency.app.core_base.shared.di.baseModule
import com.multicurrency.app.core_logger.shared.impl.di.loggerModule
import com.multicurrency.app.core_network.shared.impl.di.httpEngineModule
import com.multicurrency.app.core_network.shared.impl.di.networkModule
import com.multicurrency.app.core_preferences.shared.impl.di.preferencesModule
import com.multicurrency.app.feature_currency_converter.shared.di.featureCurrencyConverterModule
import com.multicurrency.app.feature_currency_converter.shared.di.getHomePagePlatformModule


fun getSharedModules() =
    listOf(
        baseModule,
        loggerModule,
        preferencesModule,
        networkModule,
        httpEngineModule,
        featureCurrencyConverterModule,
        getHomePagePlatformModule()
    )
