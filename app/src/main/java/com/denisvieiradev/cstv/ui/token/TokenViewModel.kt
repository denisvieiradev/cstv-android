package com.denisvieiradev.cstv.ui.token

import androidx.lifecycle.ViewModel
import com.denisvieiradev.cachemanager.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TokenViewModel(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TokenUiState())
    val uiState: StateFlow<TokenUiState> = _uiState.asStateFlow()

    fun onAction(action: TokenScreenAction) {
        when (action) {
            is TokenScreenAction.OnTokenChanged -> _uiState.update { it.copy(token = action.value) }
            is TokenScreenAction.Confirm -> saveToken()
        }
    }

    private fun saveToken() {
        val token = _uiState.value.token
        if (token.isBlank()) return
        sessionRepository.saveToken(token)
        _uiState.update { it.copy(navigateToMatches = true) }
    }
}
