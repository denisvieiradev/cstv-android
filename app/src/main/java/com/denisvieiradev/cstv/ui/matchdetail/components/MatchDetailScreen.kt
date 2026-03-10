package com.denisvieiradev.cstv.ui.matchdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.Player
import com.denisvieiradev.cstv.domain.model.Team
import com.denisvieiradev.cstv.ui.matchdetail.MatchDetailScreenAction
import com.denisvieiradev.cstv.ui.matchdetail.MatchDetailUiState
import com.denisvieiradev.cstv.ui.matchdetail.PlayersState
import com.denisvieiradev.cstv.ui.matches.util.MatchDateFormatter
import com.denisvieiradev.design_system.ui.components.maintopbar.MainTopBar
import com.denisvieiradev.design_system.ui.theme.Spacing
import java.time.LocalDate

@Composable
fun MatchDetailScreen(
    uiState: MatchDetailUiState,
    onAction: (MatchDetailScreenAction) -> Unit
) {
    val match = uiState.match
    Scaffold(
        topBar = {
            val title = if (match != null) "${match.leagueName} + ${match.serieFullName}" else ""
            MainTopBar(
                title = title,
                onNavigateBack = { onAction(MatchDetailScreenAction.NavigateBack) }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (match != null) {
                MatchDetailContent(
                    match = match,
                    playersState = uiState.playersState,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
private fun MatchDetailContent(
    match: Match,
    playersState: PlayersState,
    onAction: (MatchDetailScreenAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = Spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Spacing.large))
        MatchTeamsHeader(teamA = match.teamA, teamB = match.teamB)
        Spacer(modifier = Modifier.height(Spacing.medium))
        ScheduledTime(scheduledAt = match.scheduledAt)
        Spacer(modifier = Modifier.height(Spacing.large))
        when (playersState) {
            is PlayersState.Loading -> PlayersSkeleton()
            is PlayersState.Error -> PlayersError(onRetry = { onAction(MatchDetailScreenAction.RetryLoadPlayers) })
            is PlayersState.Success -> {
                val hasPlayers = playersState.teamA.isNotEmpty() || playersState.teamB.isNotEmpty()
                if (hasPlayers) {
                    PlayersList(teamAPlayers = playersState.teamA, teamBPlayers = playersState.teamB)
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.match_detail_game_not_started),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlayersSkeleton() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            repeat(PLAYERS_PER_TEAM) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(Spacing.small))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                )
            }
        }
        Spacer(modifier = Modifier.width(Spacing.small))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            repeat(PLAYERS_PER_TEAM) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clip(RoundedCornerShape(Spacing.small))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                )
            }
        }
    }
}

@Composable
private fun PlayersError(onRetry: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.match_detail_players_error),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.match_detail_players_retry))
            }
        }
    }
}

@Composable
private fun MatchTeamsHeader(teamA: Team?, teamB: Team?) {
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
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(R.string.match_detail_team_logo_desc, teamName),
            modifier = Modifier
                .size(logoSize)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
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
private fun ScheduledTime(scheduledAt: String?) {
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

private const val PLAYERS_PER_TEAM = 5

@Composable
private fun PlayersList(teamAPlayers: List<Player>, teamBPlayers: List<Player>) {
    val team1Players = buildFixedPlayerList(teamAPlayers)
    val team2Players = buildFixedPlayerList(teamBPlayers)
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            team1Players.forEach { player ->
                TeamOnePlayerItem(player = player)
            }
        }
        Spacer(modifier = Modifier.width(Spacing.small))
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Spacing.small)) {
            team2Players.forEach { player ->
                TeamTwoPlayerItem(player = player)
            }
        }
    }
}

private fun buildFixedPlayerList(players: List<Player>): List<Player?> {
    val base = players.take(PLAYERS_PER_TEAM)
    return base + List(PLAYERS_PER_TEAM - base.size) { null }
}

@Composable
private fun TeamOnePlayerItem(player: Player?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.small))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
            if (player != null) {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val fullName = listOfNotNull(player.firstName, player.lastName).joinToString(" ")
                if (fullName.isNotBlank()) {
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.match_detail_player_missing),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.width(Spacing.small))
        PlayerPhoto(player = player)
    }
}

@Composable
private fun TeamTwoPlayerItem(player: Player?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(Spacing.small))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(Spacing.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        PlayerPhoto(player = player)
        Spacer(modifier = Modifier.width(Spacing.small))
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.Start) {
            if (player != null) {
                Text(
                    text = player.name,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                val fullName = listOfNotNull(player.firstName, player.lastName).joinToString(" ")
                if (fullName.isNotBlank()) {
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.match_detail_player_missing),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun PlayerPhoto(player: Player?) {
    val photoSize = 48.dp
    val shape = RoundedCornerShape(Spacing.extraSmall)
    if (player == null) {
        PlayerInitialsBox("?", photoSize, shape)
        return
    }
    val initials = playerInitials(player.firstName, player.lastName, player.name)
    if (player.imageUrl != null) {
        SubcomposeAsyncImage(
            model = player.imageUrl,
            contentDescription = stringResource(R.string.match_detail_player_photo_desc, player.name),
            modifier = Modifier.size(photoSize).clip(shape),
            contentScale = ContentScale.Crop,
            error = { PlayerInitialsBox(initials, photoSize, shape) }
        )
    } else {
        PlayerInitialsBox(initials, photoSize, shape)
    }
}

@Composable
private fun PlayerInitialsBox(initials: String, size: Dp, shape: Shape) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun playerInitials(firstName: String?, lastName: String?, name: String): String {
    val first = firstName?.firstOrNull()?.uppercaseChar()
    val last = lastName?.firstOrNull()?.uppercaseChar()
    return when {
        first != null && last != null -> "$first$last"
        first != null -> "$first"
        else -> name.take(1).uppercase()
    }
}
