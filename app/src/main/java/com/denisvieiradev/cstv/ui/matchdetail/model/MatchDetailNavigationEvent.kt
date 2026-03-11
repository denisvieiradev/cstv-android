package com.denisvieiradev.cstv.ui.matchdetail.model

sealed interface MatchDetailNavigationEvent {
    data object NavigateBack : MatchDetailNavigationEvent
}
