package com.denisvieiradev.cstv.ui.matches.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.denisvieiradev.cstv.ui.matches.MatchesViewModel
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
