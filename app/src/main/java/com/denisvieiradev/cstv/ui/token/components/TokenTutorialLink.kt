package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.denisvieiradev.cstv.R
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
internal fun TokenTutorialLink(onClick: () -> Unit) {
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
