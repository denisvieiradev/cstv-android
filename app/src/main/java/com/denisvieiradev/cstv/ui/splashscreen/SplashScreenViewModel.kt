package com.denisvieiradev.cstv.ui.splashscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.ui.splashscreen.model.SplashScreenAction
import com.denisvieiradev.cstv.ui.splashscreen.model.SplashScreenNavigationEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreenViewModel(
    private val sessionLocalDataSource: SessionLocalDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _navigationEvents = Channel<SplashScreenNavigationEvent>()
    val navigationEvents: Flow<SplashScreenNavigationEvent> = _navigationEvents.receiveAsFlow()

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
            _navigationEvents.send(event)
        }
    }
}
