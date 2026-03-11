package com.denisvieiradev.cstv.ui.matchdetail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.denisvieiradev.design_system.ui.theme.CstvTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchDetailScreenRoot(
    viewModel: MatchDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CstvTheme(darkTheme = uiState.darkTheme) {
        MatchDetailScreen(
            uiState = uiState,
            onAction = viewModel::onAction
        )
    }
}
