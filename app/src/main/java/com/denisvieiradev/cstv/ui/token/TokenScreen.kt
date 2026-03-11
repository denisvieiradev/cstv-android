package com.denisvieiradev.cstv.ui.token

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.ui.token.components.TokenTutorialDialog
import com.denisvieiradev.design_system.ui.components.button.PrimaryButton
import com.denisvieiradev.design_system.ui.components.input.AppTextField
import com.denisvieiradev.design_system.ui.theme.Alpha
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
    val glowColor = MaterialTheme.colorScheme.primary.copy(alpha = Alpha.faint)

    Surface(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
            TokenScreenBackground(glowColor = glowColor)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                TokenLogo(logoGradient = logoGradient)
                Spacer(modifier = Modifier.height(Spacing.medium))
                TokenScreenHeader()
                Spacer(modifier = Modifier.height(Spacing.extraLarge))
                TokenInputCard(uiState = uiState, onAction = onAction)
                Spacer(modifier = Modifier.height(Spacing.medium))
                TokenTutorialLink(onClick = { onAction(TokenScreenAction.ShowTutorial) })
                Spacer(modifier = Modifier.height(Spacing.medium))
                TokenScreenFooter()
            }

            TokenTopBar(
                uiState = uiState,
                onAction = onAction,
                modifier = Modifier.align(Alignment.TopEnd)
            )

            if (uiState.showTutorialDialog) {
                TokenTutorialDialog(
                    onDismiss = { onAction(TokenScreenAction.DismissTutorial) },
                    onAction = onAction
                )
            }
            if (uiState.showDemoConfirmationDialog) {
                DemoConfirmationDialog(
                    onDismiss = { onAction(TokenScreenAction.DismissDemoConfirmation) },
                    onConfirm = { onAction(TokenScreenAction.TryDemo) }
                )
            }
        }
    }
}

@Composable
private fun TokenScreenBackground(glowColor: Color) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(glowColor, Color.Transparent),
                center = Offset(size.width / 2f, 0f),
                radius = size.width * 0.8f
            ),
            radius = size.width * 0.8f,
            center = Offset(size.width / 2f, 0f)
        )
    }
}

@Composable
private fun TokenLogo(logoGradient: Brush) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer { alpha = 0.99f }
    ) {
        Image(
            painter = painterResource(R.drawable.fuze_icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(brush = logoGradient, blendMode = BlendMode.SrcAtop)
        }
    }
}

@Composable
private fun TokenScreenHeader() {
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
}

@Composable
private fun TokenInputCard(
    uiState: TokenUiState,
    onAction: (TokenScreenAction) -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = RoundedCornerShape(Spacing.cornerRadiusCard),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(Spacing.medium),
            verticalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            AppTextField(
                value = uiState.token,
                onValueChange = { onAction(TokenScreenAction.OnTokenChanged(it)) },
                label = stringResource(R.string.token_field_label)
            )
            PrimaryButton(
                text = stringResource(R.string.token_confirm_button),
                onClick = { onAction(TokenScreenAction.Confirm) },
                enabled = uiState.isConfirmEnabled
            )
            if (uiState.error != null) {
                Text(
                    text = stringResource(R.string.token_error_message),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
            OutlinedButton(
                onClick = { onAction(TokenScreenAction.ShowDemoConfirmation) },
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
        }
    }
}

@Composable
private fun TokenTutorialLink(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = stringResource(R.string.token_tutorial_link),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(
                horizontal = Spacing.medium,
                vertical = Spacing.small
            )
        )
    }
}

@Composable
private fun TokenScreenFooter() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(R.string.token_screen_footer),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(Spacing.extraSmall))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.OpenInNew,
            contentDescription = null,
            modifier = Modifier.size(Spacing.iconSmall),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TokenTopBar(
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

@Composable
private fun DemoConfirmationDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(Spacing.cornerRadiusLarge),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.medium)
        ) {
            Column(
                modifier = Modifier.padding(Spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "DEMO",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = primary.copy(alpha = Alpha.faint)
                )
                Spacer(modifier = Modifier.height(Spacing.small))
                Icon(
                    imageVector = Icons.Filled.Science,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp),
                    tint = primary
                )
                Spacer(modifier = Modifier.height(Spacing.medium))
                Text(
                    text = stringResource(R.string.token_demo_confirmation_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(Spacing.small))
                Text(
                    text = stringResource(R.string.token_demo_confirmation_body),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(Spacing.large))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(3) {
                        Box(
                            modifier = Modifier
                                .height(6.dp)
                                .weight(1f)
                                .clip(CircleShape)
                                .background(primary)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.extraSmall))
                Text(
                    text = stringResource(R.string.token_demo_confirmation_limit),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(Spacing.medium))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(Spacing.small),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Info,
                            contentDescription = null,
                            modifier = Modifier.size(Spacing.iconSmall),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = stringResource(R.string.token_demo_confirmation_warning),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Spacing.large))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.token_demo_confirmation_cancel))
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = stringResource(R.string.token_demo_confirmation_start))
                    }
                }
            }
        }
    }
}
