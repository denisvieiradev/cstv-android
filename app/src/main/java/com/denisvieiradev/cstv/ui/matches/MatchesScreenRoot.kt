package com.denisvieiradev.cstv.ui.matches

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchesScreenRoot(
    viewModel: MatchesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    MatchesScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}
