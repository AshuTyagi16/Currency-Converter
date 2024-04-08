package com.multicurrency.app.core_base.shared.di

import com.multicurrency.app.core_base.shared.util.CacheExpirationUtil
import org.koin.dsl.module

val baseModule  = module {
    single {
        CacheExpirationUtil()
    }
}