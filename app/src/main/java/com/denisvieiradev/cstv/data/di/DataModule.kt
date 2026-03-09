package com.denisvieiradev.cstv.data.di

import com.denisvieiradev.cstv.data.datasources.matches.MatchApi
import com.denisvieiradev.cstv.data.datasources.matches.MatchRemoteDataSourceImpl
import com.denisvieiradev.cstv.domain.repository.MatchRepository
import org.koin.dsl.module
import retrofit2.Retrofit

val datasourcesModule = module {
    single<MatchApi> { get<Retrofit>().create(MatchApi::class.java) }
    single<MatchRepository> { MatchRemoteDataSourceImpl(get()) }
}
