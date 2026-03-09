package com.denisvieiradev.cstv.ui.matches

sealed interface MatchesScreenAction {
    data object LoadMatches : MatchesScreenAction
    data object Retry : MatchesScreenAction
}
