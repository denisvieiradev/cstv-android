package com.denisvieiradev.cstv.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Match(
    val id: Int,
    val status: MatchStatus,
    val scheduledAt: String?,
    val beginAt: String?,
    val leagueName: String,
    val serieFullName: String,
    val leagueImageUrl: String?,
    val teamA: Team?,
    val teamB: Team?
) : Parcelable
