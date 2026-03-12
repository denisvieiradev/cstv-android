package com.denisvieiradev.cstv.ui.matchdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.usecase.GetMatchDetailUseCase
import com.denisvieiradev.cstv.ui.matchdetail.model.MatchDetailNavigationEvent
import com.denisvieiradev.cstv.ui.matchdetail.model.MatchDetailScreenAction
import com.denisvieiradev.cstv.ui.matchdetail.model.MatchDetailUiState
import com.denisvieiradev.cstv.ui.matchdetail.model.PlayersState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.denisvieiradev.network.data.remote.utils.AuthorizationException
import java.io.IOException
import timber.log.Timber

class MatchDetailViewModel(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val getMatchDetailUseCase: GetMatchDetailUseCase,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    companion object {
        const val EXTRA_MATCH = "match"
    }

    private val _uiState = MutableStateFlow(MatchDetailUiState())
    val uiState: StateFlow<MatchDetailUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<MatchDetailNavigationEvent>()
    val navigationEvents: Flow<MatchDetailNavigationEvent> = _navigationEvents

    private var currentMatchId: Int? = null

    private val fetchPlayersExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        when (throwable) {
            is AuthorizationException -> {
                Timber.d(throwable, "Authorization failed in match detail")
                sessionLocalDataSource.clearSession()
                viewModelScope.launch {
                    _navigationEvents.emit(MatchDetailNavigationEvent.NavigateToTokenScreen)
                }
            }
            is IOException -> {
                Timber.e(throwable, "Network error fetching match detail for matchId=$currentMatchId")
                _uiState.update { it.copy(playersState = PlayersState.Error) }
            }
            else -> {
                Timber.e(throwable, "Failed to fetch match detail for matchId=$currentMatchId")
                _uiState.update { it.copy(playersState = PlayersState.Error) }
            }
        }
    }

    init {
        _uiState.update { it.copy(darkTheme = sessionLocalDataSource.isDarkTheme()) }
    }

    fun setMatch(match: Match) {
        if (currentMatchId != null) return
        currentMatchId = match.id
        _uiState.update {
            it.copy(match = match, playersState = PlayersState.Loading)
        }
        fetchPlayers(match.id)
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
        viewModelScope.launch(ioDispatcher + fetchPlayersExceptionHandler) {
            val match = getMatchDetailUseCase(matchId)
            val teamAPlayers = match.teamA?.players ?: emptyList()
            val teamBPlayers = match.teamB?.players ?: emptyList()
            _uiState.update { it.copy(playersState = PlayersState.Success(teamAPlayers, teamBPlayers)) }
        }
    }
}
