package com.denisvieiradev.cstv.ui.matchdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.ui.matchdetail.model.MatchDetailNavigationEvent
import com.denisvieiradev.design_system.ui.theme.CstvTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchDetailScreenRoot(
    match: Match?,
    viewModel: MatchDetailViewModel = koinViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToTokenScreen: () -> Unit
) {
    LaunchedEffect(match) {
        match?.let { viewModel.setMatch(it) }
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.navigationEvents.collect { event ->
            when (event) {
                MatchDetailNavigationEvent.NavigateBack -> onNavigateBack()
                MatchDetailNavigationEvent.NavigateToTokenScreen -> onNavigateToTokenScreen()
            }
        }
    }
    CstvTheme(darkTheme = uiState.darkTheme) {
        MatchDetailScreen(
            uiState = uiState,
            onAction = viewModel::onAction
        )
    }
}
