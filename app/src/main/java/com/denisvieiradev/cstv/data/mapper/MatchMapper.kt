package com.denisvieiradev.cstv.data.mapper

import com.denisvieiradev.cstv.data.dto.MatchDto
import com.denisvieiradev.cstv.data.dto.PlayerDto
import com.denisvieiradev.cstv.data.dto.TeamDto
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.domain.model.Player
import com.denisvieiradev.cstv.domain.model.Team

fun MatchDto.toDomain(): Match {
    val opponents = opponents.orEmpty()
    return Match(
        id = id ?: 0,
        status = status.toMatchStatus(),
        scheduledAt = scheduledAt,
        beginAt = beginAt,
        leagueName = league?.name.orEmpty(),
        serieFullName = serie?.fullName.orEmpty(),
        leagueImageUrl = league?.imageUrl,
        teamA = opponents.getOrNull(0)?.opponent?.toDomain(),
        teamB = opponents.getOrNull(1)?.opponent?.toDomain()
    )
}

private fun PlayerDto.toDomain() = Player(
    id = id ?: 0,
    name = name.orEmpty(),
    imageUrl = imageUrl,
    firstName = firstName,
    lastName = lastName
)

internal fun TeamDto.toDomain() = Team(
    id = id ?: 0,
    name = name.orEmpty(),
    imageUrl = imageUrl,
    players = players?.map { it.toDomain() }.orEmpty()
)

private fun String?.toMatchStatus(): MatchStatus = when (this) {
    "running" -> MatchStatus.RUNNING
    "not_started" -> MatchStatus.NOT_STARTED
    "finished" -> MatchStatus.FINISHED
    "canceled" -> MatchStatus.CANCELED
    "postponed" -> MatchStatus.POSTPONED
    else -> MatchStatus.NOT_STARTED
}
