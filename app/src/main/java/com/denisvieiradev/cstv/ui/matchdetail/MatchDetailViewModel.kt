package com.denisvieiradev.cstv.ui.matchdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.Player
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

sealed interface PlayersState {
    data object Loading : PlayersState
    data object Error : PlayersState
    data class Success(val teamA: List<Player>, val teamB: List<Player>) : PlayersState
}

data class MatchDetailUiState(
    val match: Match? = null,
    val darkTheme: Boolean = false,
    val playersState: PlayersState = PlayersState.Loading
)

sealed interface MatchDetailScreenAction {
    data object NavigateBack : MatchDetailScreenAction
    data object RetryLoadPlayers : MatchDetailScreenAction
}

sealed interface MatchDetailNavigationEvent {
    data object NavigateBack : MatchDetailNavigationEvent
}

class MatchDetailViewModel(
    private val selectedMatchHolder: SelectedMatchHolder,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchDetailUiState())
    val uiState: StateFlow<MatchDetailUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<MatchDetailNavigationEvent>()
    val navigationEvents: Flow<MatchDetailNavigationEvent> = _navigationEvents

    init {
        val match = selectedMatchHolder.get()
        if (match == null) {
            Timber.d("SelectedMatchHolder returned null — no match to display")
        }
        selectedMatchHolder.clear()
        _uiState.update { it.copy(match = match, darkTheme = sessionRepository.isDarkTheme()) }
    }

    fun onAction(action: MatchDetailScreenAction) {
        when (action) {
            is MatchDetailScreenAction.NavigateBack -> viewModelScope.launch {
                _navigationEvents.emit(MatchDetailNavigationEvent.NavigateBack)
            }
            is MatchDetailScreenAction.RetryLoadPlayers -> Unit
        }
    }
}
