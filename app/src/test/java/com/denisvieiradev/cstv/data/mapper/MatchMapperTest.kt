package com.denisvieiradev.cstv.data.mapper

import com.denisvieiradev.cstv.data.dto.MatchDto
import com.denisvieiradev.cstv.data.dto.OpponentWrapperDto
import com.denisvieiradev.cstv.data.dto.PlayerDto
import com.denisvieiradev.cstv.data.dto.TeamDto
import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.utils.TestConstants
import com.denisvieiradev.cstv.utils.fakeMatchDto
import com.denisvieiradev.cstv.utils.fakeTeamDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MatchMapperTest {

    @Test
    fun `should map all fields correctly when dto is complete`() {
        val dto = fakeMatchDto(
            id = 42,
            status = "running",
            scheduledAt = "2024-06-01T18:00:00Z",
            beginAt = "2024-06-01T18:05:00Z",
            leagueName = "ESL Pro League",
            leagueImageUrl = "https://example.com/league.png",
            serieFullName = "Season 20",
            teamA = fakeTeamDto(id = 10, name = "Natus Vincere", imageUrl = "https://example.com/navi.png"),
            teamB = fakeTeamDto(id = 20, name = "FaZe Clan", imageUrl = "https://example.com/faze.png")
        )

        val domain = dto.toDomain()

        assertThat(domain.id).isEqualTo(42)
        assertThat(domain.status).isEqualTo(MatchStatus.RUNNING)
        assertThat(domain.scheduledAt).isEqualTo("2024-06-01T18:00:00Z")
        assertThat(domain.beginAt).isEqualTo("2024-06-01T18:05:00Z")
        assertThat(domain.leagueName).isEqualTo("ESL Pro League")
        assertThat(domain.leagueImageUrl).isEqualTo("https://example.com/league.png")
        assertThat(domain.serieFullName).isEqualTo("Season 20")
        assertThat(domain.teamA?.id).isEqualTo(10)
        assertThat(domain.teamA?.name).isEqualTo("Natus Vincere")
        assertThat(domain.teamA?.imageUrl).isEqualTo("https://example.com/navi.png")
        assertThat(domain.teamB?.id).isEqualTo(20)
        assertThat(domain.teamB?.name).isEqualTo("FaZe Clan")
        assertThat(domain.teamB?.imageUrl).isEqualTo("https://example.com/faze.png")
    }

    @Test
    fun `should use empty string for null team name`() {
        val dto = fakeMatchDto(teamA = fakeTeamDto(name = null))

        val domain = dto.toDomain()

        assertThat(domain.teamA?.name).isEqualTo("")
    }

    @Test
    fun `should use null for missing team image url`() {
        val dto = fakeMatchDto(teamA = fakeTeamDto(imageUrl = null))

        val domain = dto.toDomain()

        assertThat(domain.teamA?.imageUrl).isNull()
    }

    @Test
    fun `should return null teamA and teamB when opponents is null`() {
        val dto = fakeMatchDto().copy(opponents = null)

        val domain = dto.toDomain()

        assertThat(domain.teamA).isNull()
        assertThat(domain.teamB).isNull()
    }

    @Test
    fun `should return teamA mapped and teamB null when only one opponent`() {
        val dto = fakeMatchDto().copy(
            opponents = listOf(OpponentWrapperDto(opponent = TeamDto(id = 1, name = "Solo Team", imageUrl = null)))
        )

        val domain = dto.toDomain()

        assertThat(domain.teamA?.name).isEqualTo("Solo Team")
        assertThat(domain.teamB).isNull()
    }

    @Test
    fun `should handle null opponent entry at index 0 without NPE`() {
        val dto = fakeMatchDto().copy(
            opponents = listOf(OpponentWrapperDto(opponent = null))
        )

        // Act / Assert — must not throw
        val domain = dto.toDomain()
        assertThat(domain.teamA).isNull()
    }

    @Test
    fun `PlayerDto toDomain maps id name and imageUrl correctly`() {
        val playerDto = PlayerDto(id = 7, name = "s1mple", imageUrl = "https://example.com/s1mple.png")
        val dto = fakeMatchDto().copy(
            opponents = listOf(
                OpponentWrapperDto(opponent = TeamDto(id = 1, name = "NaVi", imageUrl = null, players = listOf(playerDto)))
            )
        )

        val player = dto.toDomain().teamA?.players?.first()

        assertThat(player?.id).isEqualTo(7)
        assertThat(player?.name).isEqualTo("s1mple")
        assertThat(player?.imageUrl).isEqualTo("https://example.com/s1mple.png")
    }

    @Test
    fun `PlayerDto toDomain with null fields uses default values`() {
        val playerDto = PlayerDto(id = null, name = null, imageUrl = null)
        val dto = fakeMatchDto().copy(
            opponents = listOf(
                OpponentWrapperDto(opponent = TeamDto(id = 1, name = "NaVi", imageUrl = null, players = listOf(playerDto)))
            )
        )

        val player = dto.toDomain().teamA?.players?.first()

        assertThat(player?.id).isEqualTo(0)
        assertThat(player?.name).isEqualTo("")
        assertThat(player?.imageUrl).isNull()
    }

    @Test
    fun `TeamDto toDomain with players list maps players correctly`() {
        val players = listOf(
            PlayerDto(id = 1, name = "player1", imageUrl = "https://example.com/p1.png"),
            PlayerDto(id = 2, name = "player2", imageUrl = null)
        )
        val dto = fakeMatchDto().copy(
            opponents = listOf(
                OpponentWrapperDto(opponent = TeamDto(id = 10, name = "Team X", imageUrl = null, players = players))
            )
        )

        val mappedPlayers = dto.toDomain().teamA?.players

        assertThat(mappedPlayers).hasSize(2)
        assertThat(mappedPlayers?.get(0)?.name).isEqualTo("player1")
        assertThat(mappedPlayers?.get(1)?.name).isEqualTo("player2")
    }

    @Test
    fun `TeamDto toDomain with null players returns empty list`() {
        val dto = fakeMatchDto().copy(
            opponents = listOf(
                OpponentWrapperDto(opponent = TeamDto(id = 10, name = "Team X", imageUrl = null, players = null))
            )
        )

        val mappedPlayers = dto.toDomain().teamA?.players

        assertThat(mappedPlayers).isEmpty()
    }

    @Test
    fun `should map status string to correct MatchStatus enum`() {
        // Arrange / Act / Assert
        assertThat(fakeMatchDto(status = TestConstants.STATUS_RUNNING).toDomain().status).isEqualTo(MatchStatus.RUNNING)
        assertThat(fakeMatchDto(status = TestConstants.STATUS_NOT_STARTED).toDomain().status).isEqualTo(MatchStatus.NOT_STARTED)
        assertThat(fakeMatchDto(status = TestConstants.STATUS_FINISHED).toDomain().status).isEqualTo(MatchStatus.FINISHED)
        assertThat(fakeMatchDto(status = TestConstants.STATUS_CANCELED).toDomain().status).isEqualTo(MatchStatus.CANCELED)
        assertThat(fakeMatchDto(status = TestConstants.STATUS_POSTPONED).toDomain().status).isEqualTo(MatchStatus.POSTPONED)
        assertThat(fakeMatchDto(status = null).toDomain().status).isEqualTo(MatchStatus.NOT_STARTED)
    }
}
