package com.denisvieiradev.cstv.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenViewModel(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashScreenUiState())
    val uiState: StateFlow<SplashScreenUiState> = _uiState.asStateFlow()

    private val _navigationEvents = MutableSharedFlow<SplashScreenNavigationEvent>()
    val navigationEvents: Flow<SplashScreenNavigationEvent> = _navigationEvents

    fun onAction(action: SplashScreenAction) {
        when (action) {
            is SplashScreenAction.CheckSession -> checkSession()
        }
    }

    private fun checkSession() {
        viewModelScope.launch {
            val hasToken = withContext(ioDispatcher) { sessionLocalDataSource.getToken() != null }
            val event = if (hasToken) {
                SplashScreenNavigationEvent.NavigateToMatches
            } else {
                SplashScreenNavigationEvent.NavigateToToken
            }
            _navigationEvents.emit(event)
        }
    }
}
