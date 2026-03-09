package com.denisvieiradev.cstv.data.repository

import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchRemoteDataSource
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.repository.MatchRepository

class MatchRepositoryImpl(private val remoteDataSource: MatchRemoteDataSource) : MatchRepository {
    override suspend fun getRunningMatches(): List<Match> = remoteDataSource.getRunningMatches()
    override suspend fun getUpcomingMatches(): List<Match> = remoteDataSource.getUpcomingMatches()
}
