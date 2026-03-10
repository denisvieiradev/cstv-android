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
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import coil.compose.SubcomposeAsyncImage
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.model.Team
import com.denisvieiradev.cstv.ui.matches.util.MatchDateFormatter
import com.denisvieiradev.design_system.ui.theme.Spacing
import java.time.LocalDate

@Composable
internal fun MatchTeamsHeader(teamA: Team?, teamB: Team?) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TeamColumn(
            team = teamA,
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        Text(
            text = stringResource(R.string.match_vs),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = Spacing.medium)
        )
        TeamColumn(
            team = teamB,
            modifier = Modifier.weight(1f),
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
    Column(modifier = modifier, horizontalAlignment = horizontalAlignment) {
        TeamLogo(imageUrl = team?.imageUrl, teamName = team?.name ?: "TBD")
        Spacer(modifier = Modifier.height(Spacing.small))
        Text(
            text = team?.name ?: "TBD",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TeamLogo(imageUrl: String?, teamName: String) {
    val logoSize = 60.dp
    if (imageUrl != null) {
        SubcomposeAsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.match_detail_team_logo_desc, teamName),
            modifier = Modifier
                .size(logoSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            loading = {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(modifier = Modifier.size(30.dp))
                }
            }
        )
    } else {
        Box(
            modifier = Modifier
                .size(logoSize)
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
