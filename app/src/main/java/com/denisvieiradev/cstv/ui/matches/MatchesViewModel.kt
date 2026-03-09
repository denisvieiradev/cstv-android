package com.denisvieiradev.cstv.ui.matches

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import com.denisvieiradev.network.data.remote.utils.AuthorizationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchesViewModel(
    private val getCsMatchesUseCase: GetCsMatchesUseCase,
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MatchesUiState(isDarkTheme = sessionRepository.isDarkTheme())
    )
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<MatchesNavigationEvent>()
    val navigationEvents: Flow<MatchesNavigationEvent> = _navigationEvents

    init {
        loadMatches()
    }

    fun onAction(action: MatchesScreenAction) {
        when (action) {
            is MatchesScreenAction.LoadMatches -> loadMatches()
            is MatchesScreenAction.Retry -> loadMatches()
            is MatchesScreenAction.Logout -> _uiState.update { it.copy(showLogoutDialog = true) }
            is MatchesScreenAction.ConfirmLogout -> clearSessionAndNavigate()
            is MatchesScreenAction.DismissLogout -> _uiState.update { it.copy(showLogoutDialog = false) }
            is MatchesScreenAction.ConfigureToken -> clearSessionAndNavigate()
            is MatchesScreenAction.ToggleTheme -> {
                val newValue = !_uiState.value.isDarkTheme
                sessionRepository.saveDarkTheme(newValue)
                _uiState.update { it.copy(isDarkTheme = newValue) }
            }
        }
    }

    private fun loadMatches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isAuthError = false) }
            try {
                val matches = getCsMatchesUseCase()
                _uiState.update { it.copy(isLoading = false, matches = matches) }
            } catch (e: AuthorizationException) {
                _uiState.update { it.copy(isLoading = false, isAuthError = true, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e, isAuthError = false) }
            }
        }
    }

    private fun clearSessionAndNavigate() {
        viewModelScope.launch {
            sessionRepository.clearSession()
            Log.d(TAG, "Session cleared, navigating to TokenActivity")
            _uiState.update { it.copy(showLogoutDialog = false) }
            _navigationEvents.emit(MatchesNavigationEvent.NavigateToTokenScreen)
        }
    }

    companion object {
        private const val TAG = "MatchesViewModel"
    }
}
