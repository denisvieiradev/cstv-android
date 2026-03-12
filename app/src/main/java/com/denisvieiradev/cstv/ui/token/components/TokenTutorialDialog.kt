package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.ui.token.model.TokenScreenAction
import com.denisvieiradev.design_system.ui.theme.Alpha
import com.denisvieiradev.design_system.ui.theme.Spacing

private const val PANDASCORE_LOGIN_URL = "https://app.pandascore.co/login"

@Composable
fun TokenTutorialDialog(
    onDismiss: () -> Unit,
    onAction: (TokenScreenAction) -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val uriHandler = LocalUriHandler.current
    var currentStep by remember { mutableIntStateOf(0) }
    val steps = listOf(
        stringResource(R.string.token_tutorial_step_1),
        stringResource(R.string.token_tutorial_step_2),
        stringResource(R.string.token_tutorial_step_3)
    )
    val stepTitles = listOf(
        stringResource(R.string.token_tutorial_step_title_1),
        stringResource(R.string.token_tutorial_step_title_2),
        stringResource(R.string.token_tutorial_step_title_3)
    )
    val stepIcons: List<ImageVector> = listOf(
        Icons.Filled.Language,
        Icons.Filled.Key,
        Icons.Filled.ContentPaste
    )
    val isLastStep = currentStep == steps.lastIndex

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
                TutorialDialogHeader(onDismiss = onDismiss)
                Spacer(modifier = Modifier.height(Spacing.small))
                TutorialStepIndicator(stepCount = steps.size, currentStep = currentStep)
                Spacer(modifier = Modifier.height(Spacing.large))
                TutorialStepContent(
                    currentStep = currentStep,
                    stepTitles = stepTitles,
                    steps = steps,
                    stepIcons = stepIcons,
                    onOpenPandaScore = { uriHandler.openUri(PANDASCORE_LOGIN_URL) }
                )
                Spacer(modifier = Modifier.height(Spacing.large))
                TutorialNavigationRow(
                    currentStep = currentStep,
                    isLastStep = isLastStep,
                    onBack = { currentStep-- },
                    onNext = { currentStep++ },
                    onPaste = {
                        val clipText = clipboardManager.getText()?.text?.ifBlank { null }
                        onAction(TokenScreenAction.PasteTokenFromClipboard(clipText))
                    }
                )
            }
        }
    }
}

@Composable
private fun TutorialDialogHeader(onDismiss: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.token_tutorial_title),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        IconButton(onClick = onDismiss) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun TutorialStepIndicator(stepCount: Int, currentStep: Int) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(stepCount) { index ->
            val dotColor by animateColorAsState(
                targetValue = if (index == currentStep)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = Alpha.subtle),
                animationSpec = tween(300),
                label = "dotColor$index"
            )
            val dotWidth = if (index == currentStep) 24.dp else 8.dp
            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(dotWidth)
                    .clip(CircleShape)
                    .background(dotColor)
            )
        }
    }
}

