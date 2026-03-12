package com.denisvieiradev.cstv.ui.token

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.BuildConfig
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.ui.core.LocaleManager
import com.denisvieiradev.cstv.ui.core.ThemeManager
import com.denisvieiradev.cstv.ui.token.model.TokenNavigationEvent
import com.denisvieiradev.cstv.ui.token.model.TokenScreenAction
import com.denisvieiradev.cstv.ui.token.model.TokenUiState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.denisvieiradev.cstv.ui.core.stateInWhileSubscribed
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TokenViewModel(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val demoSessionManager: DemoSessionManager,
    private val themeManager: ThemeManager,
    private val localeManager: LocaleManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TokenUiState(
            isDarkTheme = sessionLocalDataSource.isDarkTheme(),
            currentLanguage = sessionLocalDataSource.getLanguage() ?: Language.EN,
            isDemoAvailable = !demoSessionManager.isDemoAlreadyUsed()
        )
    )
    val uiState: StateFlow<TokenUiState> = _uiState.stateInWhileSubscribed(
        viewModel = this,
        initialValue = TokenUiState()
    )

    private val _navigationEvents = MutableSharedFlow<TokenNavigationEvent>()
    val navigationEvents: Flow<TokenNavigationEvent> = _navigationEvents

    fun onAction(action: TokenScreenAction) {
        when (action) {
            is TokenScreenAction.OnTokenChanged -> _uiState.update { it.copy(token = action.value) }
            is TokenScreenAction.Confirm -> saveToken()
            is TokenScreenAction.TryDemo -> {
                _uiState.update { it.copy(showDemoConfirmationDialog = false) }
                enterDemoMode()
            }
            is TokenScreenAction.ToggleTheme -> toggleTheme()
            is TokenScreenAction.ToggleLanguage -> toggleLanguage()
            is TokenScreenAction.ShowTutorial -> _uiState.update { it.copy(showTutorialDialog = true) }
            is TokenScreenAction.DismissTutorial -> _uiState.update { it.copy(showTutorialDialog = false) }
            is TokenScreenAction.ShowDemoConfirmation -> _uiState.update { it.copy(showDemoConfirmationDialog = true) }
            is TokenScreenAction.DismissDemoConfirmation -> _uiState.update { it.copy(showDemoConfirmationDialog = false) }
            is TokenScreenAction.PasteTokenFromClipboard -> pasteFromClipboard(action.clipboardText)
        }
    }

    private fun saveToken() {
        val token = _uiState.value.token
        if (token.isBlank()) return
        viewModelScope.launch(ioDispatcher) {
            try {
                sessionLocalDataSource.saveToken(token)
                _navigationEvents.emit(TokenNavigationEvent.NavigateToMatches)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e) }
            }
        }
    }

    private fun toggleTheme() {
        val new = !_uiState.value.isDarkTheme
        _uiState.update { it.copy(isDarkTheme = new) }
        viewModelScope.launch(ioDispatcher) { sessionLocalDataSource.saveDarkTheme(new) }
        themeManager.apply(new)
    }

    private fun toggleLanguage() {
        val next = if (_uiState.value.currentLanguage == Language.EN) Language.PT else Language.EN
        _uiState.update { it.copy(currentLanguage = next) }
        viewModelScope.launch(ioDispatcher) { sessionLocalDataSource.saveLanguage(next) }
        val needsRecreate = localeManager.apply(next)
        if (needsRecreate) {
            viewModelScope.launch { _navigationEvents.emit(TokenNavigationEvent.RecreateActivity) }
        }
    }

    private fun enterDemoMode() {
        viewModelScope.launch(ioDispatcher) {
            demoSessionManager.startDemo()
            sessionLocalDataSource.saveToken(BuildConfig.PANDASCORE_DEMO_API_TOKEN)
            _navigationEvents.emit(TokenNavigationEvent.NavigateToMatches)
        }
    }

    private fun pasteFromClipboard(text: String?) {
        _uiState.update {
            it.copy(
                token = text?.trim() ?: it.token,
                showTutorialDialog = false
            )
        }
    }
}
