package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.ui.token.model.TokenScreenAction
import com.denisvieiradev.cstv.ui.token.model.TokenUiState
import com.denisvieiradev.design_system.ui.components.languageswitcher.LanguageOption
import com.denisvieiradev.design_system.ui.components.languageswitcher.LanguageSwitcher
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
        LanguageSwitcher(
            options = listOf(
                LanguageOption(code = Language.EN, label = Language.EN_LABEL),
                LanguageOption(code = Language.PT, label = Language.PT_LABEL)
            ),
            selectedLanguageCode = uiState.currentLanguage,
            onToggle = { onAction(TokenScreenAction.ToggleLanguage) }
        )
        IconButton(onClick = { onAction(TokenScreenAction.ToggleTheme) }) {
            Icon(
                imageVector = if (uiState.isDarkTheme) Icons.Filled.DarkMode else Icons.Filled.LightMode,
                contentDescription = stringResource(R.string.matches_theme_toggle_content_desc)
            )
        }
    }
}
