package com.denisvieiradev.cachemanager.di

import com.denisvieiradev.cachemanager.SessionRepository
import com.denisvieiradev.cachemanager.SessionRepositoryImpl
import org.koin.dsl.module

val cacheManagerModule = module {
    single<SessionRepository> { SessionRepositoryImpl() }
}
