package com.denisvieiradev.cstv.ui.matches

sealed interface MatchesNavigationEvent {
    data object NavigateToTokenScreen : MatchesNavigationEvent
    data object RecreateActivity : MatchesNavigationEvent
    data object OpenMatchDetail : MatchesNavigationEvent
}
