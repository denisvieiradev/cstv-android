package com.denisvieiradev.cstv.ui.matches

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import com.denisvieiradev.network.data.remote.utils.AuthorizationException
import timber.log.Timber
import kotlinx.coroutines.CoroutineDispatcher
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
import kotlinx.coroutines.withContext

class MatchesViewModel(
    private val getCsMatchesUseCase: GetCsMatchesUseCase,
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val demoSessionManager: DemoSessionManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        MatchesUiState(
            isDarkTheme = sessionLocalDataSource.isDarkTheme(),
            currentLanguage = sessionLocalDataSource.getLanguage() ?: Language.EN
        )
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
            is MatchesScreenAction.DismissDemoExpired -> {
                demoSessionManager.reset()
                clearSessionAndNavigate()
            }
            is MatchesScreenAction.ToggleTheme -> {
                val newValue = !_uiState.value.isDarkTheme
                _uiState.update { it.copy(isDarkTheme = newValue) }
                viewModelScope.launch(ioDispatcher) { sessionLocalDataSource.saveDarkTheme(newValue) }
            }
            is MatchesScreenAction.OpenMatchDetail -> viewModelScope.launch {
                _navigationEvents.emit(MatchesNavigationEvent.OpenMatchDetail(action.match))
            }
            is MatchesScreenAction.ToggleLanguage -> {
                val next = if (_uiState.value.currentLanguage == Language.EN) Language.PT else Language.EN
                _uiState.update { it.copy(currentLanguage = next) }
                viewModelScope.launch(ioDispatcher) { sessionLocalDataSource.saveLanguage(next) }
                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(next))
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    viewModelScope.launch { _navigationEvents.emit(MatchesNavigationEvent.RecreateActivity) }
                }
            }
        }
    }

    private fun loadMatches() {
        if (!demoSessionManager.tryConsume(2)) {
            _uiState.update { it.copy(isLoading = false, showDemoExpiredDialog = true) }
            return
        }
        _uiState.update { it.copy(isLoading = true, error = null, isAuthError = false) }
        flow { emit(getCsMatchesUseCase()) }
            .flowOn(ioDispatcher)
            .onEach { matches ->
                _uiState.update { it.copy(isLoading = false, matches = matches) }
            }
            .catch { e ->
                when (e) {
                    is AuthorizationException -> _uiState.update {
                        it.copy(isLoading = false, isAuthError = true, error = null)
                    }
                    else -> _uiState.update {
                        it.copy(isLoading = false, error = e, isAuthError = false)
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun clearSessionAndNavigate() {
        viewModelScope.launch {
            withContext(ioDispatcher) { sessionLocalDataSource.clearSession() }
            Timber.d("Session cleared, navigating to TokenActivity")
            _uiState.update { it.copy(showLogoutDialog = false) }
            _navigationEvents.emit(MatchesNavigationEvent.NavigateToTokenScreen)
        }
    }

}
