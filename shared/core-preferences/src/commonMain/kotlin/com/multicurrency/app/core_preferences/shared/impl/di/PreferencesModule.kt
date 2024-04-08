package com.multicurrency.app.core_preferences.shared.impl.di

import com.multicurrency.app.core_preferences.shared.api.PreferenceApi
import com.multicurrency.app.core_preferences.shared.impl.PreferenceApiImpl
import com.russhwolf.settings.MapSettings
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val preferencesModule = module {
    single<PreferenceApi> {
        PreferenceApiImpl(settings = Settings())
    }
}

val testPreferencesModule = module {
    single<Settings> {
        MapSettings()
    }
    single<PreferenceApi> {
        PreferenceApiImpl(settings = get())
    }
}