package com.denisvieiradev.cstv.data.datasources.matches

import com.denisvieiradev.cstv.data.mapper.toDomain
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.repository.MatchRepository

class MatchRemoteDataSourceImpl(private val api: MatchApi) : MatchRepository {
    override suspend fun getRunningMatches(): List<Match> = api.getRunningMatches().map { it.toDomain() }
    override suspend fun getUpcomingMatches(): List<Match> = api.getUpcomingMatches().map { it.toDomain() }
}
