package com.denisvieiradev.cstv.ui.matchdetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.ui.matchdetail.model.MatchDetailScreenAction
import com.denisvieiradev.cstv.ui.matchdetail.model.MatchDetailUiState
import com.denisvieiradev.cstv.ui.matchdetail.model.PlayersState
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.ui.matchdetail.components.MatchTeamsHeader
import com.denisvieiradev.cstv.ui.matchdetail.components.PlayersError
import com.denisvieiradev.cstv.ui.matchdetail.components.PlayersList
import com.denisvieiradev.cstv.ui.matchdetail.components.PlayersSkeleton
import com.denisvieiradev.cstv.ui.matchdetail.components.ScheduledTime
import com.denisvieiradev.design_system.ui.components.maintopbar.MainTopBar
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
fun MatchDetailScreen(
    uiState: MatchDetailUiState,
    onAction: (MatchDetailScreenAction) -> Unit
) {
    val match = uiState.match
    Scaffold(
        topBar = {
            val title = if (match != null) "${match.leagueName} • ${match.serieFullName}" else ""
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
            is PlayersState.Idle -> Box {}
            is PlayersState.Loading -> PlayersSkeleton()
            is PlayersState.Error -> PlayersError(onRetry = { onAction(MatchDetailScreenAction.RetryLoadPlayers) })
            is PlayersState.Success -> {
                val hasPlayers = playersState.teamA.isNotEmpty() || playersState.teamB.isNotEmpty()
                if (hasPlayers) {
                    PlayersList(
                        teamAPlayers = playersState.teamA,
                        teamBPlayers = playersState.teamB
                    )
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
