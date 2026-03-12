package com.denisvieiradev.cstv.ui.matches

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.denisvieiradev.design_system.ui.theme.CstvTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MatchesScreenRoot(
    viewModel: MatchesViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CstvTheme(darkTheme = uiState.isDarkTheme) {
        MatchesScreen(
            uiState = uiState,
            onAction = viewModel::onAction
        )
    }
}
