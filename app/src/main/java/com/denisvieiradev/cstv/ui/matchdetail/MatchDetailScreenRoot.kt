package com.denisvieiradev.cstv.ui.matchdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchDetailScreenRoot(
    viewModel: MatchDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MatchDetailScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}
