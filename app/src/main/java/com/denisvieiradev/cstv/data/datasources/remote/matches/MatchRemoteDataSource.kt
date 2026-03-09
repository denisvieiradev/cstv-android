package com.denisvieiradev.cstv.data.datasources.remote.matches

import com.denisvieiradev.cstv.domain.model.Match

interface MatchRemoteDataSource {
    suspend fun getRunningMatches(): List<Match>
    suspend fun getUpcomingMatches(): List<Match>
}
