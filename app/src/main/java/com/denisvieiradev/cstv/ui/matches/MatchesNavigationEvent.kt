package com.denisvieiradev.cstv.ui.matches

sealed interface MatchesNavigationEvent {
    data object NavigateToTokenScreen : MatchesNavigationEvent
}
