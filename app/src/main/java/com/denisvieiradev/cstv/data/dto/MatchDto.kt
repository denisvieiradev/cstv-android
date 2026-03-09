package com.denisvieiradev.cstv.data.dto

import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("status") val status: String?,
    @SerializedName("scheduled_at") val scheduledAt: String?,
    @SerializedName("begin_at") val beginAt: String?,
    @SerializedName("league") val league: LeagueDto?,
    @SerializedName("serie") val serie: SerieDto?,
    @SerializedName("opponents") val opponents: List<OpponentWrapperDto>?
)

data class LeagueDto(
    @SerializedName("name") val name: String?,
    @SerializedName("image_url") val imageUrl: String?
)

data class SerieDto(
    @SerializedName("full_name") val fullName: String?
)

data class OpponentWrapperDto(
    @SerializedName("opponent") val opponent: TeamDto?
)

data class TeamDto(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("image_url") val imageUrl: String?
)
