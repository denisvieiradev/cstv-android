package com.denisvieiradev.cstv.ui.matchdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.model.Team
import com.denisvieiradev.cstv.ui.matches.util.MatchDateFormatter
import com.denisvieiradev.design_system.ui.components.image.NetworkImage
import com.denisvieiradev.design_system.ui.theme.Alpha
import com.denisvieiradev.design_system.ui.theme.Spacing
import com.denisvieiradev.design_system.ui.theme.Weight
import java.time.LocalDate

@Composable
internal fun MatchTeamsHeader(teamA: Team?, teamB: Team?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamColumn(
            team = teamA,
            modifier = Modifier.weight(Weight.equal),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        Text(
            text = stringResource(R.string.match_vs),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = Alpha.dim),
            modifier = Modifier.padding(horizontal = Spacing.medium)
        )
        TeamColumn(
            team = teamB,
            modifier = Modifier.weight(Weight.equal),
            horizontalAlignment = Alignment.CenterHorizontally
        )
    }
}

@Composable
private fun TeamColumn(
    team: Team?,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
) {
    val teamName = team?.name ?: stringResource(R.string.match_tbd)
    Column(modifier = modifier, horizontalAlignment = horizontalAlignment) {
        NetworkImage(
            imageUrl = team?.imageUrl,
            contentDescription = stringResource(R.string.match_detail_team_logo_desc, teamName),
            size = Spacing.teamLogoSize,
            contentScale = ContentScale.Crop,
            fallback = {
                Box(
                    modifier = Modifier
                        .size(Spacing.teamLogoSize)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = teamName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        Text(
            text = teamName,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
internal fun ScheduledTime(scheduledAt: String?) {
    val formatted = MatchDateFormatter.format(
        scheduledAt = scheduledAt,
        now = LocalDate.now()
    )
    Text(
        text = formatted,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center
    )
}
