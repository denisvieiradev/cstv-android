package com.denisvieiradev.cstv.domain.di

import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import com.denisvieiradev.cstv.domain.usecase.GetMatchDetailUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::GetCsMatchesUseCase)
    factoryOf(::GetMatchDetailUseCase)
}
