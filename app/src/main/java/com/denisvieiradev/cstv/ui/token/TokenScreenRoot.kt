package com.denisvieiradev.cstv.ui.token

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.denisvieiradev.design_system.ui.theme.CstvTheme

@Composable
fun TokenScreenRoot(
    viewModel: TokenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CstvTheme(darkTheme = uiState.isDarkTheme) {
        TokenScreen(
            uiState = uiState,
            onAction = viewModel::onAction,
            modifier = modifier
        )
    }
}
