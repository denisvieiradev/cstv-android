package com.denisvieiradev.cstv.ui.matches.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.denisvieiradev.cstv.R
import com.denisvieiradev.design_system.ui.components.maintopbar.MainTopBar

@Composable
fun MatchesTopBar(
    isDarkTheme: Boolean,
    onThemeToggleClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    MainTopBar(
        title = stringResource(R.string.matches_title),
        actions = {
            IconButton(onClick = onThemeToggleClick) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = stringResource(R.string.matches_theme_toggle_content_desc)
                )
            }
            IconButton(onClick = onLogoutClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = stringResource(R.string.matches_logout_content_desc)
                )
            }
        }
    )
}
