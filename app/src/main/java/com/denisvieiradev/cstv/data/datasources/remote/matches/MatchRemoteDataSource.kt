package com.denisvieiradev.cstv.data.datasources.remote.matches

import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.Team

interface MatchRemoteDataSource {
    suspend fun getRunningMatches(): List<Match>
    suspend fun getUpcomingMatches(): List<Match>
    suspend fun getMatchDetail(matchId: Int): Match
    suspend fun getTeamWithPlayers(teamId: Int): Team
}
