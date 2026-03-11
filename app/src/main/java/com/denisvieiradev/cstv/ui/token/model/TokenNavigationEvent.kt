package com.denisvieiradev.cstv.ui.token.model

sealed interface TokenNavigationEvent {
    data object RecreateActivity : TokenNavigationEvent
    data object NavigateToMatches : TokenNavigationEvent
}
