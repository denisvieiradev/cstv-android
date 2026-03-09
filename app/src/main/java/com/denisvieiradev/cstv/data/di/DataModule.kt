package com.denisvieiradev.cstv.data.di

import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.denisvieiradev.cstv.data.datasources.local.SessionRepositoryImpl
import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchApi
import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchRemoteDataSource
import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchRemoteDataSourceImpl
import com.denisvieiradev.cstv.data.repository.MatchRepositoryImpl
import com.denisvieiradev.cstv.domain.repository.MatchRepository
import com.denisvieiradev.network.data.remote.utils.TokenProvider
import org.koin.dsl.module
import retrofit2.Retrofit

val datasourcesModule = module {
    // Local
    single<SessionRepository> { SessionRepositoryImpl(get()) }
    // TokenProvider bridge to core:network's AuthInterceptor
    single<TokenProvider> { TokenProvider { get<SessionRepository>().getToken() } }
    // Remote
    single<MatchApi> { get<Retrofit>().create(MatchApi::class.java) }
    single<MatchRemoteDataSource> { MatchRemoteDataSourceImpl(get()) }
    single<MatchRepository> { MatchRepositoryImpl(get()) }
}
