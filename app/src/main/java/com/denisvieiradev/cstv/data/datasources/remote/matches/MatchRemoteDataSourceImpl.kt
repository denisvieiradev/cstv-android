package com.denisvieiradev.cstv.data.datasources.remote.matches

import com.denisvieiradev.cstv.data.mapper.toDomain
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.Team

class MatchRemoteDataSourceImpl(private val api: MatchApi) : MatchRemoteDataSource {
    override suspend fun getRunningMatches(): List<Match> = api.getRunningMatches().map { it.toDomain() }
    override suspend fun getUpcomingMatches(): List<Match> = api.getUpcomingMatches().map { it.toDomain() }
    override suspend fun getMatchDetail(matchId: Int): Match = api.getMatchDetail(matchId).toDomain()
    override suspend fun getTeamWithPlayers(teamId: Int): Team = api.getTeamDetail(teamId).toDomain()
}
