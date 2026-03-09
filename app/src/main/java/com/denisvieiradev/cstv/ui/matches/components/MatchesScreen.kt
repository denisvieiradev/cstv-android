package com.denisvieiradev.cstv.ui.matches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.ui.matches.MatchesScreenAction
import com.denisvieiradev.cstv.ui.matches.MatchesUiState
import com.denisvieiradev.design_system.ui.components.button.PrimaryButton
import com.denisvieiradev.design_system.ui.components.maintopbar.MainTopBar
import com.denisvieiradev.design_system.ui.theme.Spacing
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val scheduledAtFormatter = DateTimeFormatter
    .ofPattern("dd.MM HH:mm")
    .withZone(ZoneId.systemDefault())

@Composable
fun MatchesScreen(
    uiState: MatchesUiState,
    onAction: (MatchesScreenAction) -> Unit
) {
    Scaffold(
        topBar = { MainTopBar(title = stringResource(R.string.matches_title)) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> LoadingContent()
                uiState.hasError -> ErrorContent(onRetry = { onAction(MatchesScreenAction.Retry) })
                uiState.isEmpty -> EmptyContent()
                else -> MatchesList(matches = uiState.matches)
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.matches_error_title),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.small))
        Text(
            text = stringResource(R.string.matches_error_body),
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.large))
        PrimaryButton(
            text = stringResource(R.string.matches_retry),
            onClick = onRetry,
            modifier = Modifier.padding(horizontal = Spacing.large)
        )
    }
}

@Composable
private fun EmptyContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.matches_empty),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(Spacing.large)
        )
    }
}

@Composable
private fun MatchesList(matches: List<Match>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(Spacing.medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        items(matches, key = { it.id }) { match ->
            MatchCard(match = match)
        }
    }
}

@Composable
private fun MatchCard(match: Match) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            StatusBadge(
                status = match.status,
                scheduledAt = match.scheduledAt,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = Spacing.small, end = Spacing.small)
            )

            TeamsRow(
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

            LeagueInfo(
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

@Composable
private fun StatusBadge(
    status: MatchStatus,
    scheduledAt: String?,
    modifier: Modifier = Modifier
) {
    if (status == MatchStatus.RUNNING) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomStart = 8.dp))
                .background(MaterialTheme.colorScheme.error)
                .padding(horizontal = Spacing.small, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.match_status_live),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onError
            )
        }
    } else {
        val formattedTime = scheduledAt?.formatScheduledAt() ?: ""
        Box(
            modifier = modifier.padding(horizontal = Spacing.small, vertical = 4.dp)
        ) {
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TeamsRow(
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
        TeamLogo(
            name = teamAName ?: "TBD",
            imageUrl = teamAImageUrl,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = stringResource(R.string.match_vs),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = Spacing.small)
        )

        TeamLogo(
            name = teamBName ?: "TBD",
            imageUrl = teamBImageUrl,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TeamLogo(
    name: String,
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(2).uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.height(Spacing.extraSmall))
        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LeagueInfo(
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
            AsyncImage(
                model = leagueImageUrl,
                contentDescription = leagueName,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(16.dp)
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

private fun String.formatScheduledAt(): String {
    return try {
        val instant = Instant.parse(this)
        scheduledAtFormatter.format(instant)
    } catch (e: Exception) {
        this
    }
}
