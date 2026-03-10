package com.denisvieiradev.cstv.ui.token

import androidx.lifecycle.ViewModel
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TokenViewModel(
    private val sessionLocalDataSource: SessionLocalDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(TokenUiState())
    val uiState: StateFlow<TokenUiState> = _uiState.asStateFlow()

    fun onAction(action: TokenScreenAction) {
        when (action) {
            is TokenScreenAction.OnTokenChanged -> _uiState.update { it.copy(token = action.value) }
            is TokenScreenAction.Confirm -> saveToken()
        }
    }

    fun onNavigationConsumed() {
        _uiState.update { it.copy(navigateToMatches = false) }
    }

    private fun saveToken() {
        val token = _uiState.value.token
        if (token.isBlank()) return
        try {
            sessionLocalDataSource.saveToken(token)
            _uiState.update { it.copy(navigateToMatches = true) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e) }
        }
    }
}
