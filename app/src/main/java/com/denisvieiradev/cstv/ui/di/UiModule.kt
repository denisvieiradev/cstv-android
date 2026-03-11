package com.denisvieiradev.cstv.ui.di

import com.denisvieiradev.cstv.ui.matchdetail.MatchDetailViewModel
import com.denisvieiradev.cstv.ui.core.AppCompatLocaleManager
import com.denisvieiradev.cstv.ui.core.AppCompatThemeManager
import com.denisvieiradev.cstv.ui.core.LocaleManager
import com.denisvieiradev.cstv.ui.core.ThemeManager
import com.denisvieiradev.cstv.ui.matches.MatchesViewModel
import com.denisvieiradev.cstv.ui.splashscreen.SplashScreenViewModel
import com.denisvieiradev.cstv.ui.token.TokenViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    single<ThemeManager> { AppCompatThemeManager() }
    single<LocaleManager> { AppCompatLocaleManager() }
    viewModel { SplashScreenViewModel(get()) }
    viewModel { TokenViewModel(get(), get(), themeManager = get(), localeManager = get()) }
    viewModel { MatchesViewModel(get(), get(), get(), themeManager = get(), localeManager = get()) }
    viewModel { MatchDetailViewModel(get(), get(), get()) }
}
