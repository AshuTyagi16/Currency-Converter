package com.multicurrency.app.shared

import com.multicurrency.app.core_logger.shared.api.LoggerApi
import com.multicurrency.app.core_network.shared.api.HttpClientApi
import com.multicurrency.app.core_preferences.shared.api.PreferenceApi
import com.multicurrency.app.feature_currency_converter.shared.ui.HomePageViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object SharedModuleDependencies : KoinComponent {

    val httpClientApi by inject<HttpClientApi>()

    val preferencesApi by inject<PreferenceApi>()

    val loggerApi by inject<LoggerApi>()

    val homePageViewModel by inject<HomePageViewModel>()

}