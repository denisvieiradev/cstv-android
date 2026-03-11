package com.denisvieiradev.cstv.ui.matchdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.Player
import com.denisvieiradev.cstv.domain.usecase.GetMatchDetailUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

sealed interface PlayersState {
    data object Idle : PlayersState
    data object Loading : PlayersState
    data object Error : PlayersState
    data class Success(val teamA: List<Player>, val teamB: List<Player>) : PlayersState
}

data class MatchDetailUiState(
    val match: Match? = null,
    val darkTheme: Boolean = false,
    val playersState: PlayersState = PlayersState.Idle
)

sealed interface MatchDetailScreenAction {
    data object NavigateBack : MatchDetailScreenAction
    data object RetryLoadPlayers : MatchDetailScreenAction
}

sealed interface MatchDetailNavigationEvent {
    data object NavigateBack : MatchDetailNavigationEvent
}

class MatchDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val getMatchDetailUseCase: GetMatchDetailUseCase
) : ViewModel() {

    companion object {
        const val EXTRA_MATCH = "match"
    }

    private val _uiState = MutableStateFlow(MatchDetailUiState())
    val uiState: StateFlow<MatchDetailUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<MatchDetailNavigationEvent>()
    val navigationEvents: Flow<MatchDetailNavigationEvent> = _navigationEvents

    private var currentMatchId: Int? = null

    init {
        val match: Match? = savedStateHandle[EXTRA_MATCH]
        if (match == null) {
            Timber.d("No match in SavedStateHandle — no match to display")
        }
        _uiState.update {
            it.copy(
                match = match,
                darkTheme = sessionLocalDataSource.isDarkTheme(),
                playersState = if (match == null) PlayersState.Idle else PlayersState.Loading
            )
        }
        if (match != null) {
            currentMatchId = match.id
            fetchPlayers(match.id)
        }
    }

    fun onAction(action: MatchDetailScreenAction) {
        when (action) {
            is MatchDetailScreenAction.NavigateBack -> viewModelScope.launch {
                _navigationEvents.emit(MatchDetailNavigationEvent.NavigateBack)
            }
            is MatchDetailScreenAction.RetryLoadPlayers -> {
                val matchId = currentMatchId ?: return
                fetchPlayers(matchId)
            }
        }
    }

    private fun fetchPlayers(matchId: Int) {
        _uiState.update { it.copy(playersState = PlayersState.Loading) }
        Timber.d("Fetching match detail for matchId=$matchId")
        flow { emit(getMatchDetailUseCase(matchId)) }
            .flowOn(Dispatchers.IO)
            .onEach { match ->
                val teamAPlayers = match.teamA?.players ?: emptyList()
                val teamBPlayers = match.teamB?.players ?: emptyList()
                _uiState.update { it.copy(playersState = PlayersState.Success(teamAPlayers, teamBPlayers)) }
            }
            .catch { e ->
                Timber.e(e, "Failed to fetch match detail for matchId=$matchId")
                _uiState.update { it.copy(playersState = PlayersState.Error) }
            }
            .launchIn(viewModelScope)
    }
}
