package com.denisvieiradev.cstv.ui.matches

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.ui.matches.model.MatchesScreenAction
import com.denisvieiradev.cstv.ui.matches.model.MatchesUiState
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.ui.matches.components.AuthErrorContent
import com.denisvieiradev.cstv.ui.matches.components.LogoutConfirmationDialog
import com.denisvieiradev.cstv.ui.matches.components.MatchCard
import com.denisvieiradev.cstv.ui.matches.components.MatchesTopBar
import com.denisvieiradev.design_system.ui.components.state.FullScreenError
import com.denisvieiradev.design_system.ui.components.state.FullScreenLoading
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun MatchesScreen(
    uiState: MatchesUiState,
    onAction: (MatchesScreenAction) -> Unit
) {
    Box {
        Scaffold(
            topBar = {
                MatchesTopBar(
                    isDarkTheme = uiState.isDarkTheme,
                    currentLanguage = uiState.currentLanguage,
                    onThemeToggleClick = { onAction(MatchesScreenAction.ToggleTheme) },
                    onLanguageToggleClick = { onAction(MatchesScreenAction.ToggleLanguage) },
                    onLogoutClick = { onAction(MatchesScreenAction.Logout) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when {
                    uiState.isLoading -> FullScreenLoading()
                    uiState.isAuthError -> AuthErrorContent(
                        onConfigureTokenClick = { onAction(MatchesScreenAction.ConfigureToken) }
                    )
                    uiState.hasError -> FullScreenError(
                        title = stringResource(R.string.matches_error_title),
                        body = stringResource(R.string.matches_error_body),
                        retryLabel = stringResource(R.string.matches_retry),
                        onRetry = { onAction(MatchesScreenAction.Retry) }
                    )
                    uiState.isEmpty -> EmptyContent()
                    else -> MatchesList(matches = uiState.matches, onAction = onAction)
                }
            }
        }

        if (uiState.showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirm = { onAction(MatchesScreenAction.ConfirmLogout) },
                onDismiss = { onAction(MatchesScreenAction.DismissLogout) }
            )
        }

        if (uiState.showDemoExpiredDialog) {
            DemoExpiredDialog(
                onConfirm = { onAction(MatchesScreenAction.DismissDemoExpired) }
            )
        }
    }
}

@Composable
private fun EmptyContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.matches_empty),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(Spacing.large)
        )
    }
}

@Composable
private fun DemoExpiredDialog(onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onConfirm,
        title = { Text(text = stringResource(R.string.matches_demo_expired_title)) },
        text = { Text(text = stringResource(R.string.matches_demo_expired_body)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.matches_demo_expired_cta))
            }
        }
    )
}

@Composable
private fun MatchesList(matches: List<Match>, onAction: (MatchesScreenAction) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        items(matches, key = { it.id }) { match ->
            MatchCard(
                match = match,
                onClick = { onAction(MatchesScreenAction.OpenMatchDetail(match)) })
        }
    }
}
