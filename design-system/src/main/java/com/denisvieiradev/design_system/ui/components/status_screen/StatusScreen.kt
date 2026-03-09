package com.denisvieiradev.design_system.ui.components.status_screen

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
import androidx.compose.ui.text.style.TextAlign
import com.denisvieiradev.design_system.ui.components.button.PrimaryButton
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun StatusScreen(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null
) {
    Column(
        modifier = modifier.fillMaxSize().padding(Spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(Spacing.medium))
        Text(text = message, style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center)
        if (actionLabel != null && onAction != null) {
            Spacer(modifier = Modifier.height(Spacing.large))
            PrimaryButton(text = actionLabel, onClick = onAction)
        }
    }
}
