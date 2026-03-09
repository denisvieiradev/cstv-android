package com.denisvieiradev.cachemanager.di

import com.denisvieiradev.cachemanager.CacheManager
import com.denisvieiradev.cachemanager.CacheManagerImpl
import com.denisvieiradev.cachemanager.SecureStorage
import com.denisvieiradev.cachemanager.SecureStorageImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val cacheManagerModule = module {
    single<CacheManager> { CacheManagerImpl() }
    single<SecureStorage> { SecureStorageImpl(androidContext()) }
}
