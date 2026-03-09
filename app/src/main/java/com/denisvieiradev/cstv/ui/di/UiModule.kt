package com.denisvieiradev.cstv.ui.di

import com.denisvieiradev.cstv.ui.matches.MatchesViewModel
import com.denisvieiradev.cstv.ui.token.TokenViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModelOf(::TokenViewModel)
    viewModelOf(::MatchesViewModel)
}
