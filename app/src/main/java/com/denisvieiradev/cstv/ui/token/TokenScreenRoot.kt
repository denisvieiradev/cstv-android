package com.denisvieiradev.cstv.ui.token

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.denisvieiradev.cstv.ui.matches.MatchesActivity
import com.denisvieiradev.design_system.ui.theme.CstvTheme

@Composable
fun TokenScreenRoot(
    viewModel: TokenViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.navigateToMatches) {
        if (uiState.navigateToMatches) {
            navigateToMatches(context)
            viewModel.onNavigationConsumed()
        }
    }

    CstvTheme(darkTheme = uiState.isDarkTheme) {
        TokenScreen(
            uiState = uiState,
            onAction = viewModel::onAction,
            modifier = modifier
        )
    }
}

private fun navigateToMatches(context: Context) {
    context.startActivity(Intent(context, MatchesActivity::class.java))
}
