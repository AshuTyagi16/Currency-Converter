package com.multicurrency.app

import android.app.Application
import com.multicurrency.app.shared.di.getSharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MultiCurrencyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MultiCurrencyApp)
            androidLogger()
            modules(getSharedModules())
        }
    }
}