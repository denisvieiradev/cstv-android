package com.denisvieiradev.cstv.domain.repository

import com.denisvieiradev.cstv.domain.model.Match

interface MatchRepository {
    suspend fun getRunningMatches(): List<Match>
    suspend fun getUpcomingMatches(): List<Match>
}
