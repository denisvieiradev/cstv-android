package com.denisvieiradev.cstv.ui.token

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.denisvieiradev.cstv.R
import com.denisvieiradev.design_system.ui.components.button.PrimaryButton
import com.denisvieiradev.design_system.ui.components.input.AppTextField
import com.denisvieiradev.design_system.ui.theme.DarkPrimary
import com.denisvieiradev.design_system.ui.theme.DarkTertiary
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun TokenScreen(
    uiState: TokenUiState,
    onAction: (TokenScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val logoGradient = Brush.linearGradient(
        colors = listOf(DarkPrimary, DarkTertiary),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )
    Column(
        modifier = modifier
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
        Spacer(modifier = Modifier.height(Spacing.extraLarge))
        Text(
            text = stringResource(R.string.token_screen_footer),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
