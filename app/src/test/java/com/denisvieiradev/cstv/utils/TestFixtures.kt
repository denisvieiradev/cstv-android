package com.denisvieiradev.cstv.utils

import com.denisvieiradev.cstv.data.dto.LeagueDto
import com.denisvieiradev.cstv.data.dto.MatchDto
import com.denisvieiradev.cstv.data.dto.OpponentWrapperDto
import com.denisvieiradev.cstv.data.dto.SerieDto
import com.denisvieiradev.cstv.data.dto.TeamDto
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.domain.model.Player
import com.denisvieiradev.cstv.domain.model.Team

fun fakeMatch(
    id: Int = TestConstants.TEAM_A_ID,
    status: MatchStatus = MatchStatus.NOT_STARTED,
    scheduledAt: String? = TestConstants.DEFAULT_SCHEDULED_AT,
    beginAt: String? = null,
    leagueName: String = TestConstants.DEFAULT_LEAGUE_NAME,
    serieFullName: String = TestConstants.DEFAULT_SERIE_NAME,
    leagueImageUrl: String? = TestConstants.URL_LEAGUE_IMAGE,
    teamA: Team? = fakeTeam(id = TestConstants.TEAM_A_ID, name = TestConstants.TEAM_NAME_A),
    teamB: Team? = fakeTeam(id = TestConstants.TEAM_B_ID, name = TestConstants.TEAM_NAME_B)
) = Match(
    id = id,
    status = status,
    scheduledAt = scheduledAt,
    beginAt = beginAt,
    leagueName = leagueName,
    serieFullName = serieFullName,
    leagueImageUrl = leagueImageUrl,
    teamA = teamA,
    teamB = teamB
)

fun fakeTeam(
    id: Int = TestConstants.TEAM_A_ID,
    name: String = TestConstants.TEAM_NAME_A,
    imageUrl: String? = TestConstants.URL_TEAM_IMAGE,
    players: List<Player> = emptyList()
) = Team(id = id, name = name, imageUrl = imageUrl, players = players)

fun fakePlayer(
    id: Int = TestConstants.TEAM_A_ID,
    name: String = TestConstants.DEFAULT_PLAYER_NAME,
    imageUrl: String? = TestConstants.URL_PLAYER_IMAGE,
    firstName: String? = null,
    lastName: String? = null
) = Player(id = id, name = name, imageUrl = imageUrl, firstName = firstName, lastName = lastName)

fun fakeTeamDto(
    id: Int? = TestConstants.TEAM_A_ID,
    name: String? = TestConstants.TEAM_NAME_A,
    imageUrl: String? = TestConstants.URL_TEAM_IMAGE
) = TeamDto(id = id, name = name, imageUrl = imageUrl)

fun fakeMatchDto(
    id: Int? = TestConstants.TEAM_A_ID,
    status: String? = TestConstants.STATUS_NOT_STARTED,
    scheduledAt: String? = TestConstants.DEFAULT_SCHEDULED_AT,
    beginAt: String? = null,
    leagueName: String? = TestConstants.DEFAULT_LEAGUE_NAME,
    leagueImageUrl: String? = TestConstants.URL_LEAGUE_IMAGE,
    serieFullName: String? = TestConstants.DEFAULT_SERIE_NAME,
    teamA: TeamDto = fakeTeamDto(id = TestConstants.TEAM_A_ID, name = TestConstants.TEAM_NAME_A, imageUrl = TestConstants.URL_TEAM_A_IMAGE),
    teamB: TeamDto = fakeTeamDto(id = TestConstants.TEAM_B_ID, name = TestConstants.TEAM_NAME_B, imageUrl = TestConstants.URL_TEAM_B_IMAGE)
) = MatchDto(
    id = id,
    status = status,
    scheduledAt = scheduledAt,
    beginAt = beginAt,
    league = LeagueDto(name = leagueName, imageUrl = leagueImageUrl),
    serie = SerieDto(fullName = serieFullName),
    opponents = listOf(
        OpponentWrapperDto(opponent = teamA),
        OpponentWrapperDto(opponent = teamB)
    )
)
