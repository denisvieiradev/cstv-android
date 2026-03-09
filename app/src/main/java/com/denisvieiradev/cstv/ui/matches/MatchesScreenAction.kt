package com.denisvieiradev.cstv.ui.matches

sealed interface MatchesScreenAction {
    data object LoadMatches : MatchesScreenAction
    data object Retry : MatchesScreenAction
    data object Logout : MatchesScreenAction
    data object ConfirmLogout : MatchesScreenAction
    data object DismissLogout : MatchesScreenAction
    data object ConfigureToken : MatchesScreenAction
    data object ToggleTheme : MatchesScreenAction
}
