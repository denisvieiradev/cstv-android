package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.ui.token.TokenScreenAction
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
        "Access PandaScore",
        "Copy Token",
        "Paste Token"
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

@Composable
private fun TutorialStepContent(
    currentStep: Int,
    stepTitles: List<String>,
    steps: List<String>,
    stepIcons: List<ImageVector>,
    onOpenPandaScore: () -> Unit
) {
    AnimatedContent(
        targetState = currentStep,
        transitionSpec = {
            if (targetState > initialState) {
                slideInHorizontally { it } + fadeIn() togetherWith
                        slideOutHorizontally { -it } + fadeOut()
            } else {
                slideInHorizontally { -it } + fadeIn() togetherWith
                        slideOutHorizontally { it } + fadeOut()
            }
        },
        label = "stepContent"
    ) { step ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "0${step + 1}",
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary.copy(alpha = Alpha.faint)
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            Icon(
                imageVector = stepIcons[step],
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(Spacing.medium))
            Text(
                text = stepTitles[step],
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(Spacing.small))
            Text(
                text = steps[step],
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (step == 0) {
                Spacer(modifier = Modifier.height(Spacing.small))
                OutlinedButton(
                    onClick = onOpenPandaScore,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(Spacing.iconSmall)
                    )
                    Spacer(modifier = Modifier.width(Spacing.small))
                    Text(text = stringResource(R.string.token_tutorial_open_pandascore))
                }
            }
        }
    }
}

@Composable
private fun TutorialNavigationRow(
    currentStep: Int,
    isLastStep: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
    onPaste: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.small)
    ) {
        if (currentStep > 0) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "← Back")
            }
        }
        if (isLastStep) {
            Button(
                onClick = onPaste,
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.token_tutorial_paste))
            }
        } else {
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "Next →")
            }
        }
    }
}
