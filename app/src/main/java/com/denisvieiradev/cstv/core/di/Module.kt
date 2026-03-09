package com.denisvieiradev.cstv.core.di

import com.denisvieiradev.cachemanager.di.cacheManagerModule
import com.denisvieiradev.cstv.data.di.datasourcesModule
import com.denisvieiradev.cstv.domain.di.domainModule
import com.denisvieiradev.cstv.ui.di.uiModule
import com.denisvieiradev.network.di.networkModule
import org.koin.dsl.module

val appModule = module {
    // Global singletons
}

val getModules = listOf(
    appModule,
    uiModule,
    networkModule,
    cacheManagerModule,
    datasourcesModule,
    domainModule
)
