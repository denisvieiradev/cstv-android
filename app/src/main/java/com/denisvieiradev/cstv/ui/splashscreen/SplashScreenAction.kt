package com.denisvieiradev.cstv.ui.splashscreen

sealed interface SplashScreenAction {
    data object CheckSession : SplashScreenAction
}
