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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.model.Player
import com.denisvieiradev.design_system.ui.theme.Spacing

private const val PLAYERS_PER_TEAM = 5

@Composable
internal fun PlayersList(teamAPlayers: List<Player>, teamBPlayers: List<Player>) {
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

@Composable
internal fun PlayersSkeleton() {
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
internal fun PlayersError(onRetry: () -> Unit) {
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

private fun buildFixedPlayerList(players: List<Player>): List<Player?> {
    val base = players.take(PLAYERS_PER_TEAM)
    return base + List(PLAYERS_PER_TEAM - base.size) { null }
}
