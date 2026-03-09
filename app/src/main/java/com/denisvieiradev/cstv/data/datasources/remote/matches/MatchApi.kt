package com.denisvieiradev.cstv.data.datasources.remote.matches

import com.denisvieiradev.cstv.data.dto.MatchDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MatchApi {
    @GET("csgo/matches/running")
    suspend fun getRunningMatches(): List<MatchDto>

    @GET("csgo/matches/upcoming")
    suspend fun getUpcomingMatches(@Query("sort") sort: String = "scheduled_at"): List<MatchDto>
}
