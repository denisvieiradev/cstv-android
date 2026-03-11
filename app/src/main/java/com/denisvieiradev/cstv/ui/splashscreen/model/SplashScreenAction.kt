package com.denisvieiradev.cstv.ui.splashscreen.model

sealed interface SplashScreenAction {
    data object CheckSession : SplashScreenAction
}
