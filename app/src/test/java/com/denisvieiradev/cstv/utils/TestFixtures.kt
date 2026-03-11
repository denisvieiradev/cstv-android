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
    id: Int = 1,
    status: MatchStatus = MatchStatus.NOT_STARTED,
    scheduledAt: String? = "2024-01-01T00:00:00Z",
    beginAt: String? = null,
    leagueName: String = "ESL Pro League",
    serieFullName: String = "Season 20",
    leagueImageUrl: String? = "https://example.com/league.png",
    teamA: Team? = fakeTeam(id = 1, name = "Team A"),
    teamB: Team? = fakeTeam(id = 2, name = "Team B")
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
    id: Int = 1,
    name: String = "Team A",
    imageUrl: String? = "https://example.com/team.png",
    players: List<Player> = emptyList()
) = Team(id = id, name = name, imageUrl = imageUrl, players = players)

fun fakePlayer(
    id: Int = 1,
    name: String = "s1mple",
    imageUrl: String? = "https://example.com/player.png",
    firstName: String? = null,
    lastName: String? = null
) = Player(id = id, name = name, imageUrl = imageUrl, firstName = firstName, lastName = lastName)

fun fakeTeamDto(
    id: Int? = 1,
    name: String? = "Team A",
    imageUrl: String? = "https://example.com/team.png"
) = TeamDto(id = id, name = name, imageUrl = imageUrl)

fun fakeMatchDto(
    id: Int? = 1,
    status: String? = "not_started",
    scheduledAt: String? = "2024-01-01T00:00:00Z",
    beginAt: String? = null,
    leagueName: String? = "ESL Pro League",
    leagueImageUrl: String? = "https://example.com/league.png",
    serieFullName: String? = "Season 20",
    teamA: TeamDto = fakeTeamDto(id = 1, name = "Team A", imageUrl = "https://example.com/team_a.png"),
    teamB: TeamDto = fakeTeamDto(id = 2, name = "Team B", imageUrl = "https://example.com/team_b.png")
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
