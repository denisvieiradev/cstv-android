package com.denisvieiradev.cstv.ui.splashscreen.model

sealed interface SplashScreenNavigationEvent {
    data object NavigateToMatches : SplashScreenNavigationEvent
    data object NavigateToToken : SplashScreenNavigationEvent
}
