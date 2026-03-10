package com.denisvieiradev.cstv.data.mapper

import com.denisvieiradev.cstv.data.dto.MatchDto
import com.denisvieiradev.cstv.data.dto.PlayerDto
import com.denisvieiradev.cstv.data.dto.TeamDto
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.domain.model.Player
import com.denisvieiradev.cstv.domain.model.Team

private object MatchStatusValue {
    const val RUNNING = "running"
    const val NOT_STARTED = "not_started"
    const val FINISHED = "finished"
    const val CANCELED = "canceled"
    const val POSTPONED = "postponed"
}

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
    MatchStatusValue.RUNNING -> MatchStatus.RUNNING
    MatchStatusValue.NOT_STARTED -> MatchStatus.NOT_STARTED
    MatchStatusValue.FINISHED -> MatchStatus.FINISHED
    MatchStatusValue.CANCELED -> MatchStatus.CANCELED
    MatchStatusValue.POSTPONED -> MatchStatus.POSTPONED
    else -> MatchStatus.NOT_STARTED
}
