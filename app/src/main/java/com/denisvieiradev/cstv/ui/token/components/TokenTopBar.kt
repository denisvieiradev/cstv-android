package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.ui.token.TokenScreenAction
import com.denisvieiradev.cstv.ui.token.TokenUiState
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
internal fun TokenTopBar(
    uiState: TokenUiState,
    onAction: (TokenScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .statusBarsPadding()
            .padding(top = Spacing.medium, end = Spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(onClick = {
            if (uiState.currentLanguage != Language.EN) onAction(TokenScreenAction.ToggleLanguage)
        }) {
            Text(
                text = "EN",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (uiState.currentLanguage == Language.EN) FontWeight.Bold else FontWeight.Normal,
                color = if (uiState.currentLanguage == Language.EN)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = "|",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = {
            if (uiState.currentLanguage != Language.PT) onAction(TokenScreenAction.ToggleLanguage)
        }) {
            Text(
                text = "PT",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (uiState.currentLanguage == Language.PT) FontWeight.Bold else FontWeight.Normal,
                color = if (uiState.currentLanguage == Language.PT)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = { onAction(TokenScreenAction.ToggleTheme) }) {
            Icon(
                imageVector = if (uiState.isDarkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                contentDescription = stringResource(R.string.matches_theme_toggle_content_desc)
            )
        }
    }
}
