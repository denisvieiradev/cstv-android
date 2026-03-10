package com.denisvieiradev.cstv.ui.token

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.design_system.ui.components.button.PrimaryButton
import com.denisvieiradev.design_system.ui.components.input.AppTextField
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun TokenScreen(
    uiState: TokenUiState,
    onAction: (TokenScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val logoGradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.tertiary
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "CS",
                style = MaterialTheme.typography.displayMedium.copy(
                    brush = logoGradient,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(Spacing.medium))
            Text(
                text = stringResource(R.string.token_screen_title),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            Text(
                text = stringResource(R.string.token_screen_body),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(Spacing.extraLarge))
            AppTextField(
                value = uiState.token,
                onValueChange = { onAction(TokenScreenAction.OnTokenChanged(it)) },
                label = stringResource(R.string.token_field_label)
            )
            Spacer(modifier = Modifier.height(Spacing.medium))
            PrimaryButton(
                text = stringResource(R.string.token_confirm_button),
                onClick = { onAction(TokenScreenAction.Confirm) },
                enabled = uiState.isConfirmEnabled
            )
            if (uiState.error != null) {
                Spacer(modifier = Modifier.height(Spacing.small))
                Text(
                    text = stringResource(R.string.token_error_message),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(Spacing.medium))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.token_or_divider),
                    modifier = Modifier.padding(horizontal = Spacing.medium),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                HorizontalDivider(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(Spacing.medium))
            OutlinedButton(
                onClick = { onAction(TokenScreenAction.TryDemo) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.token_demo_button),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(Spacing.medium))
            TextButton(onClick = { onAction(TokenScreenAction.ShowTutorial) }) {
                Text(
                    text = stringResource(R.string.token_tutorial_link),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(Spacing.small))
            Text(
                text = stringResource(R.string.token_screen_footer),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(modifier = Modifier.align(Alignment.TopEnd)) {
            TextButton(onClick = { onAction(TokenScreenAction.ToggleLanguage) }) {
                Text(
                    text = if (uiState.currentLanguage == Language.EN) "PT" else "EN",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            IconButton(onClick = { onAction(TokenScreenAction.ToggleTheme) }) {
                Icon(
                    imageVector = if (uiState.isDarkTheme) Icons.Filled.LightMode else Icons.Filled.DarkMode,
                    contentDescription = stringResource(R.string.matches_theme_toggle_content_desc)
                )
            }
        }

        if (uiState.showTutorialDialog) {
            TokenTutorialDialog(
                onDismiss = { onAction(TokenScreenAction.DismissTutorial) },
                onAction = onAction
            )
        }
    }
}

@Composable
private fun TokenTutorialDialog(
    onDismiss: () -> Unit,
    onAction: (TokenScreenAction) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    var currentStep by remember { mutableIntStateOf(0) }
    val steps = listOf(
        stringResource(R.string.token_tutorial_step_1),
        stringResource(R.string.token_tutorial_step_2),
        stringResource(R.string.token_tutorial_step_3),
        stringResource(R.string.token_tutorial_step_4)
    )
    val isLastStep = currentStep == steps.lastIndex

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.token_tutorial_title),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null
                    )
                }
            }
        },
        text = {
            Text(
                text = "${currentStep + 1}. ${steps[currentStep]}",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            if (isLastStep) {
                Button(onClick = {
                    val clipText = clipboardManager.getText()?.text?.ifBlank { null }
                    onAction(TokenScreenAction.PasteTokenFromClipboard(clipText))
                }) {
                    Text(stringResource(R.string.token_tutorial_paste))
                }
            } else {
                TextButton(onClick = { currentStep++ }) {
                    Text(stringResource(R.string.token_tutorial_understood))
                }
            }
        }
    )
}
