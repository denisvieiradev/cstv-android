package com.denisvieiradev.cstv.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import com.denisvieiradev.cstv.ui.matches.model.MatchesNavigationEvent
import com.denisvieiradev.cstv.ui.matches.model.MatchesScreenAction
import com.denisvieiradev.cstv.ui.core.LocaleManager
import com.denisvieiradev.cstv.ui.core.ThemeManager
import com.denisvieiradev.cstv.ui.matches.model.MatchesUiState
import com.denisvieiradev.network.data.remote.utils.AuthorizationException
import timber.log.Timber
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("LongParameterList")
class MatchesViewModel(
    private val getCsMatchesUseCase: GetCsMatchesUseCase,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val demoSessionManager: DemoSessionManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val themeManager: ThemeManager,
    private val localeManager: LocaleManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MatchesUiState(
            isDarkTheme = sessionLocalDataSource.isDarkTheme(),
            currentLanguage = sessionLocalDataSource.getLanguage() ?: Language.EN
        )
    )
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()

    private val _navigationEvents = Channel<MatchesNavigationEvent>()
    val navigationEvents: Flow<MatchesNavigationEvent> = _navigationEvents.receiveAsFlow()

    init {
        loadMatches()
    }

    fun onAction(action: MatchesScreenAction) {
        when (action) {
            is MatchesScreenAction.LoadMatches -> loadMatches()
            is MatchesScreenAction.Retry -> loadMatches()
            is MatchesScreenAction.PressLogout -> _uiState.update { it.copy(showLogoutDialog = true) }
            is MatchesScreenAction.ConfirmLogout -> clearSessionAndNavigate()
            is MatchesScreenAction.DismissLogout -> _uiState.update { it.copy(showLogoutDialog = false) }
            is MatchesScreenAction.ConfigureToken -> clearSessionAndNavigate()
            is MatchesScreenAction.DismissDemoExpired -> dismissDemoExpiredAndNavigate()
            is MatchesScreenAction.ToggleTheme -> toggleTheme()
            is MatchesScreenAction.OpenMatchDetail -> openMatchDetail(action.match)
            is MatchesScreenAction.ToggleLanguage -> toggleLanguage()
        }
    }

    private fun dismissDemoExpiredAndNavigate() {
        demoSessionManager.reset()
        clearSessionAndNavigate()
    }

    private fun toggleTheme() {
        val newValue = !_uiState.value.isDarkTheme
        _uiState.update { it.copy(isDarkTheme = newValue) }
        viewModelScope.launch(ioDispatcher) { sessionLocalDataSource.saveDarkTheme(newValue) }
        themeManager.apply(newValue)
    }

    private fun openMatchDetail(match: Match) {
        if (!demoSessionManager.tryConsume()) {
            _uiState.update { it.copy(showDemoExpiredDialog = true) }
            return
        }
        viewModelScope.launch {
            _navigationEvents.send(MatchesNavigationEvent.OpenMatchDetail(match))
        }
    }

    private fun toggleLanguage() {
        val next = if (_uiState.value.currentLanguage == Language.EN) Language.PT else Language.EN
        _uiState.update { it.copy(currentLanguage = next) }
        viewModelScope.launch(ioDispatcher) { sessionLocalDataSource.saveLanguage(next) }
        val needsRecreate = localeManager.apply(next)
        if (needsRecreate) {
            viewModelScope.launch { _navigationEvents.send(MatchesNavigationEvent.RecreateActivity) }
        }
    }

    private fun loadMatches() {
        _uiState.update { it.copy(isLoading = true, error = null, isAuthError = false) }
        viewModelScope.launch(ioDispatcher) {
            try {
                val matches = getCsMatchesUseCase()
                _uiState.update { it.copy(isLoading = false, matches = matches) }
            } catch (e: AuthorizationException) {
                Timber.d(e, "Authorization failed")
                _uiState.update { it.copy(isLoading = false, isAuthError = true, error = null) }
            } catch (e: IOException) {
                _uiState.update { it.copy(isLoading = false, error = e, isAuthError = false) }
            } catch (e: Exception) {
                Timber.e(e, "Unexpected error loading matches")
                _uiState.update { it.copy(isLoading = false, error = e, isAuthError = false) }
            }
        }
    }

    private fun clearSessionAndNavigate() {
        viewModelScope.launch {
            withContext(ioDispatcher) { sessionLocalDataSource.clearSession() }
            Timber.d("Session cleared, navigating to TokenActivity")
            _uiState.update { it.copy(showLogoutDialog = false) }
            _navigationEvents.send(MatchesNavigationEvent.NavigateToTokenScreen)
        }
    }
}
