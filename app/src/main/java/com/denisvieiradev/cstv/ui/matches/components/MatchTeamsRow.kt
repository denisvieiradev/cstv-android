package com.denisvieiradev.cstv.ui.matches.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.denisvieiradev.cstv.R
import com.denisvieiradev.design_system.ui.theme.Spacing
import com.denisvieiradev.design_system.ui.theme.Weight

@Composable
fun MatchTeamsRow(
    teamAName: String?,
    teamAImageUrl: String?,
    teamBName: String?,
    teamBImageUrl: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamDisplay(
            name = teamAName ?: "TBD",
            imageUrl = teamAImageUrl,
            modifier = Modifier.weight(Weight.equal)
        )
        Text(
            text = stringResource(R.string.match_vs),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = Spacing.small)
        )
        TeamDisplay(
            name = teamBName ?: "TBD",
            imageUrl = teamBImageUrl,
            modifier = Modifier.weight(Weight.equal)
        )
    }
}
