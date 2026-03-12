package com.denisvieiradev.cstv.ui.matchdetail.model

import com.denisvieiradev.cstv.domain.model.Player

sealed interface PlayersState {
    data object Idle : PlayersState
    data object Loading : PlayersState
    data object Error : PlayersState
    data class Success(val teamA: List<Player>, val teamB: List<Player>) : PlayersState
}
