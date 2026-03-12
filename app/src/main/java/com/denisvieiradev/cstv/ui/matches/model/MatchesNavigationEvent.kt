package com.denisvieiradev.cstv.ui.matches.model

import com.denisvieiradev.cstv.domain.model.Match

sealed interface MatchesNavigationEvent {
    data object NavigateToTokenScreen : MatchesNavigationEvent
    data object RecreateActivity : MatchesNavigationEvent
    data class OpenMatchDetail(val match: Match) : MatchesNavigationEvent
}
