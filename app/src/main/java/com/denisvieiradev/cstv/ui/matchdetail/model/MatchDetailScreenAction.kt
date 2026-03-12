package com.denisvieiradev.cstv.ui.matchdetail.model

sealed interface MatchDetailScreenAction {
    data object NavigateBack : MatchDetailScreenAction
    data object RetryLoadPlayers : MatchDetailScreenAction
}
