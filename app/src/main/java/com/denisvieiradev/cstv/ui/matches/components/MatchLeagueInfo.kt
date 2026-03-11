package com.denisvieiradev.cstv.ui.matches.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.denisvieiradev.design_system.ui.components.image.NetworkImage
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun MatchLeagueInfo(
    leagueName: String,
    serieFullName: String,
    leagueImageUrl: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leagueImageUrl != null) {
            NetworkImage(
                imageUrl = leagueImageUrl,
                contentDescription = leagueName,
                size = Spacing.iconSmall,
                fallback = {}
            )
            Spacer(modifier = Modifier.width(Spacing.extraSmall))
        }
        val leagueText = buildString {
            append(leagueName)
            if (serieFullName.isNotEmpty()) append(" · $serieFullName")
        }
        Text(
            text = leagueText,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}
