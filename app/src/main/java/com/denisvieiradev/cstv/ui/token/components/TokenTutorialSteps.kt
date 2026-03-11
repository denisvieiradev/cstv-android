package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.denisvieiradev.cstv.R
import com.denisvieiradev.design_system.ui.theme.Alpha
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
internal fun TutorialStepContent(
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
internal fun TutorialNavigationRow(
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
