package com.denisvieiradev.cstv.ui.matches

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.ui.matches.model.MatchesNavigationEvent
import com.denisvieiradev.design_system.ui.theme.CstvTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchesScreenRoot(
    viewModel: MatchesViewModel = koinViewModel(),
    onNavigateToMatchDetail: (Match) -> Unit,
    onNavigateToTokenScreen: () -> Unit,
    onRecreateActivity: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                is MatchesNavigationEvent.OpenMatchDetail -> onNavigateToMatchDetail(event.match)
                MatchesNavigationEvent.NavigateToTokenScreen -> onNavigateToTokenScreen()
                MatchesNavigationEvent.RecreateActivity -> onRecreateActivity()
            }
        }
    }
    CstvTheme(darkTheme = uiState.isDarkTheme) {
        MatchesScreen(
            uiState = uiState,
            onAction = viewModel::onAction
        )
    }
}
