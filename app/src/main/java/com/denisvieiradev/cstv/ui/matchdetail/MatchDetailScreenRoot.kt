package com.denisvieiradev.cstv.ui.matchdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchDetailScreenRoot(
    viewModel: MatchDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    MatchDetailScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}
