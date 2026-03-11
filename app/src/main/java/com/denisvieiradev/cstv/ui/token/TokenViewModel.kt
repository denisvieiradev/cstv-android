package com.denisvieiradev.cstv.ui.token

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.BuildConfig
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.Language
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TokenViewModel(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val demoSessionManager: DemoSessionManager,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        TokenUiState(
            isDarkTheme = sessionLocalDataSource.isDarkTheme(),
            currentLanguage = sessionLocalDataSource.getLanguage() ?: Language.EN
        )
    )
    val uiState: StateFlow<TokenUiState> = _uiState.asStateFlow()

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

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigateToMatches = false) }
    }

    private fun saveToken() {
        val token = _uiState.value.token
        if (token.isBlank()) return
        _uiState.update { it.copy(navigateToMatches = true) }
        viewModelScope.launch(ioDispatcher) {
            try {
                sessionLocalDataSource.saveToken(token)
            } catch (e: Exception) {
                _uiState.update { it.copy(navigateToMatches = false, error = e) }
            }
        }
    }

    private fun toggleTheme() {
        val new = !_uiState.value.isDarkTheme
        _uiState.update { it.copy(isDarkTheme = new) }
        viewModelScope.launch(ioDispatcher) { sessionLocalDataSource.saveDarkTheme(new) }
    }

    private fun toggleLanguage() {
        val next = if (_uiState.value.currentLanguage == Language.EN) Language.PT else Language.EN
        _uiState.update { it.copy(currentLanguage = next) }
        viewModelScope.launch(ioDispatcher) { sessionLocalDataSource.saveLanguage(next) }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(next))
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            viewModelScope.launch { _navigationEvents.emit(TokenNavigationEvent.RecreateActivity) }
        }
    }

    private fun enterDemoMode() {
        demoSessionManager.startDemo()
        _uiState.update { it.copy(navigateToMatches = true) }
        viewModelScope.launch(ioDispatcher) {
            sessionLocalDataSource.saveToken(BuildConfig.PANDASCORE_DEMO_API_TOKEN)
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
