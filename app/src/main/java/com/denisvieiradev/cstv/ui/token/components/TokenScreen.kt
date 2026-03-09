package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.ui.token.TokenScreenAction
import com.denisvieiradev.cstv.ui.token.TokenUiState
import com.denisvieiradev.design_system.ui.components.button.PrimaryButton
import com.denisvieiradev.design_system.ui.components.input.AppTextField
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun TokenScreen(
    uiState: TokenUiState,
    onAction: (TokenScreenAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.token_screen_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        Text(
            text = stringResource(R.string.token_screen_body),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.large))
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
    }
}
