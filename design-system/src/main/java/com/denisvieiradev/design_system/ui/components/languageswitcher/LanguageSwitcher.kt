package com.denisvieiradev.design_system.ui.components.languageswitcher

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

private const val LANGUAGE_SEPARATOR = "|"

@Composable
fun LanguageSwitcher(
    options: List<LanguageOption>,
    selectedLanguageCode: String,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    options.forEachIndexed { index, option ->
        val isSelected = option.code == selectedLanguageCode
        TextButton(
            onClick = { if (!isSelected) onToggle() },
            modifier = modifier
        ) {
            Text(
                text = option.label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (index < options.lastIndex) {
            Text(
                text = LANGUAGE_SEPARATOR,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
