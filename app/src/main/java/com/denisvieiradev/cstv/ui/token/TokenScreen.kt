package com.denisvieiradev.cstv.ui.token

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.denisvieiradev.cstv.ui.token.components.DemoConfirmationDialog
import com.denisvieiradev.cstv.ui.token.components.TokenInputCard
import com.denisvieiradev.cstv.ui.token.components.TokenLogo
import com.denisvieiradev.cstv.ui.token.components.TokenScreenBackground
import com.denisvieiradev.cstv.ui.token.components.TokenScreenFooter
import com.denisvieiradev.cstv.ui.token.components.TokenScreenHeader
import com.denisvieiradev.cstv.ui.token.components.TokenTopBar
import com.denisvieiradev.cstv.ui.token.components.TokenTutorialDialog
import com.denisvieiradev.cstv.ui.token.components.TokenTutorialLink
import com.denisvieiradev.design_system.ui.theme.Alpha
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun TokenScreen(
    uiState: TokenUiState,
    onAction: (TokenScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
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
                TokenLogo()
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
