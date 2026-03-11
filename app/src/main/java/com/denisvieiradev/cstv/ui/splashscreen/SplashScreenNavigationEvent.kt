package com.denisvieiradev.cstv.ui.splashscreen

sealed interface SplashScreenNavigationEvent {
    data object NavigateToMatches : SplashScreenNavigationEvent
    data object NavigateToToken : SplashScreenNavigationEvent
}
