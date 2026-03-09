package com.denisvieiradev.cstv.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MatchesViewModel(private val getCsMatchesUseCase: GetCsMatchesUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()

    init {
        loadMatches()
    }

    fun onAction(action: MatchesScreenAction) {
        when (action) {
            is MatchesScreenAction.LoadMatches -> loadMatches()
            is MatchesScreenAction.Retry -> loadMatches()
        }
    }

    private fun loadMatches() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val matches = getCsMatchesUseCase()
                _uiState.update { it.copy(isLoading = false, matches = matches) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e) }
            }
        }
    }
}
