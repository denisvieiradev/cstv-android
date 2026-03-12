package com.denisvieiradev.cstv.ui.matchdetail.model

import com.denisvieiradev.cstv.domain.model.Match

data class MatchDetailUiState(
    val match: Match? = null,
    val darkTheme: Boolean = false,
    val playersState: PlayersState = PlayersState.Idle
)
