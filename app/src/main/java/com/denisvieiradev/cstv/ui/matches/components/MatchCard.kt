package com.denisvieiradev.cstv.ui.matches.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun MatchCard(match: Match, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            MatchStatusBadge(
                status = match.status,
                scheduledAt = match.scheduledAt,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = Spacing.small, end = Spacing.small)
            )
            MatchTeamsRow(
                teamAName = match.teamA?.name,
                teamAImageUrl = match.teamA?.imageUrl,
                teamBName = match.teamB?.name,
                teamBImageUrl = match.teamB?.imageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.medium, vertical = Spacing.small)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Spacing.medium),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
            MatchLeagueInfo(
                leagueName = match.leagueName,
                serieFullName = match.serieFullName,
                leagueImageUrl = match.leagueImageUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = Spacing.medium, vertical = Spacing.small)
            )
        }
    }
}
