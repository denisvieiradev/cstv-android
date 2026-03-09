package com.denisvieiradev.cachemanager.di

import com.denisvieiradev.cachemanager.CacheManager
import com.denisvieiradev.cachemanager.CacheManagerImpl
import org.koin.dsl.module

val cacheManagerModule = module {
    single<CacheManager> { CacheManagerImpl() }
}
