package com.denisvieiradev.cstv.domain.usecase

import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.repository.MatchRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetMatchDetailUseCase(private val repository: MatchRepository) {
    suspend operator fun invoke(matchId: Int): Match = coroutineScope {
        val match = repository.getMatchDetail(matchId)
        val teamADeferred = if (match.teamA != null && match.teamA.players.isEmpty())
            async { repository.getTeamWithPlayers(match.teamA.id) } else null
        val teamBDeferred = if (match.teamB != null && match.teamB.players.isEmpty())
            async { repository.getTeamWithPlayers(match.teamB.id) } else null
        match.copy(
            teamA = teamADeferred?.await() ?: match.teamA,
            teamB = teamBDeferred?.await() ?: match.teamB
        )
    }
}
