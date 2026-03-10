package com.denisvieiradev.cstv.data.di

import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSourceImpl
import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchApi
import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchRemoteDataSource
import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchRemoteDataSourceImpl
import com.denisvieiradev.cstv.data.repository.MatchRepositoryImpl
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.repository.MatchRepository
import com.denisvieiradev.network.data.remote.utils.TokenProvider
import org.koin.dsl.module
import retrofit2.Retrofit

val datasourcesModule = module {
    // Local
    single<SessionLocalDataSource> { SessionLocalDataSourceImpl(get()) }
    // TokenProvider bridge to core:network's AuthInterceptor
    single<TokenProvider> { TokenProvider { get<SessionLocalDataSource>().getToken() } }
    // Remote
    single<MatchApi> { get<Retrofit>().create(MatchApi::class.java) }
    single<MatchRemoteDataSource> { MatchRemoteDataSourceImpl(get()) }
    single<MatchRepository> { MatchRepositoryImpl(get()) }
    // Demo
    single { DemoSessionManager() }
}
