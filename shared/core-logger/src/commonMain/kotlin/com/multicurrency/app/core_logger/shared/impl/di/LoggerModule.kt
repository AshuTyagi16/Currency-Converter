package com.multicurrency.app.core_logger.shared.impl.di

import com.multicurrency.app.core_logger.shared.CoreLoggerBuildKonfig
import com.multicurrency.app.core_logger.shared.api.LoggerApi
import com.multicurrency.app.core_logger.shared.impl.LoggerApiImpl
import org.koin.dsl.module

val loggerModule  = module {
    single<LoggerApi> {
        LoggerApiImpl(shouldEnableLogs = CoreLoggerBuildKonfig.IS_DEBUG)
    }
}