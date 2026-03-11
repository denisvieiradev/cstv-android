package com.denisvieiradev.cstv.ui.di

import com.denisvieiradev.cstv.ui.matchdetail.MatchDetailViewModel
import com.denisvieiradev.cstv.ui.matches.MatchesViewModel
import com.denisvieiradev.cstv.ui.token.TokenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val uiModule = module {
    viewModel { TokenViewModel(get(), get()) }
    viewModel { MatchesViewModel(get(), get(), get()) }
    viewModelOf(::MatchDetailViewModel)
}
