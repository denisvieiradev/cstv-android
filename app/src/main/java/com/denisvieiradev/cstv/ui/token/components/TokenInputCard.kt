package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.ui.token.model.TokenScreenAction
import com.denisvieiradev.cstv.ui.token.model.TokenUiState
import com.denisvieiradev.design_system.ui.components.button.PrimaryButton
import com.denisvieiradev.design_system.ui.components.input.AppTextField
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
internal fun TokenInputCard(
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
                enabled = uiState.isDemoAvailable,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Spacing.huge),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(
                        if (uiState.isDemoAvailable) R.string.token_demo_button
                        else R.string.token_demo_button_used
                    ),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
