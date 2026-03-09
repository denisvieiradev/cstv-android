package com.denisvieiradev.cstv.ui.matches.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.ui.matches.MatchesScreenAction
import com.denisvieiradev.cstv.ui.matches.MatchesUiState
import com.denisvieiradev.design_system.ui.components.button.PrimaryButton
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
                    uiState.isLoading -> LoadingContent()
                    uiState.isAuthError -> AuthErrorContent(
                        onConfigureTokenClick = { onAction(MatchesScreenAction.ConfigureToken) }
                    )
                    uiState.hasError -> ErrorContent(
                        onRetry = { onAction(MatchesScreenAction.Retry) }
                    )
                    uiState.isEmpty -> EmptyContent()
                    else -> MatchesList(matches = uiState.matches)
                }
            }
        }

        if (uiState.showLogoutDialog) {
            LogoutConfirmationDialog(
                onConfirm = { onAction(MatchesScreenAction.ConfirmLogout) },
                onDismiss = { onAction(MatchesScreenAction.DismissLogout) }
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.matches_error_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        Text(
            text = stringResource(R.string.matches_error_body),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.large))
        PrimaryButton(
            text = stringResource(R.string.matches_retry),
            onClick = onRetry,
            modifier = Modifier.padding(horizontal = Spacing.large)
        )
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
private fun MatchesList(matches: List<Match>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        items(matches, key = { it.id }) { match ->
            MatchCard(match = match)
        }
    }
}
