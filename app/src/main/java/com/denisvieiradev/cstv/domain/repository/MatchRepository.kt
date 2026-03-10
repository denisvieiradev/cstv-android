package com.denisvieiradev.cstv.domain.repository

import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.Team

interface MatchRepository {
    suspend fun getRunningMatches(): List<Match>
    suspend fun getUpcomingMatches(): List<Match>
    suspend fun getMatchDetail(matchId: Int): Match
    suspend fun getTeamWithPlayers(teamId: Int): Team
}
