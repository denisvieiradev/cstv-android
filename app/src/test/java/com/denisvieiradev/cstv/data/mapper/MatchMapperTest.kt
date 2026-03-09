package com.denisvieiradev.cstv.data.mapper

import com.denisvieiradev.cstv.data.dto.MatchDto
import com.denisvieiradev.cstv.data.dto.OpponentWrapperDto
import com.denisvieiradev.cstv.data.dto.TeamDto
import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.utils.fakeMatchDto
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MatchMapperTest {

    @Test
    fun `should map all fields correctly when dto is complete`() {
        // Arrange
        val dto = fakeMatchDto(
            id = 42,
            status = "running",
            scheduledAt = "2024-06-01T18:00:00Z",
            beginAt = "2024-06-01T18:05:00Z",
            leagueName = "ESL Pro League",
            leagueImageUrl = "https://example.com/league.png",
            serieFullName = "Season 20",
            teamAId = 10,
            teamAName = "Natus Vincere",
            teamAImageUrl = "https://example.com/navi.png",
            teamBId = 20,
            teamBName = "FaZe Clan",
            teamBImageUrl = "https://example.com/faze.png"
        )

        // Act
        val domain = dto.toDomain()

        // Assert
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
        // Arrange
        val dto = fakeMatchDto(teamAName = null)

        // Act
        val domain = dto.toDomain()

        // Assert
        assertThat(domain.teamA?.name).isEqualTo("")
    }

    @Test
    fun `should use null for missing team image url`() {
        // Arrange
        val dto = fakeMatchDto(teamAImageUrl = null)

        // Act
        val domain = dto.toDomain()

        // Assert
        assertThat(domain.teamA?.imageUrl).isNull()
    }

    @Test
    fun `should return null teamA and teamB when opponents is null`() {
        // Arrange
        val dto = fakeMatchDto().copy(opponents = null)

        // Act
        val domain = dto.toDomain()

        // Assert
        assertThat(domain.teamA).isNull()
        assertThat(domain.teamB).isNull()
    }

    @Test
    fun `should return teamA mapped and teamB null when only one opponent`() {
        // Arrange
        val dto = fakeMatchDto().copy(
            opponents = listOf(OpponentWrapperDto(opponent = TeamDto(id = 1, name = "Solo Team", imageUrl = null)))
        )

        // Act
        val domain = dto.toDomain()

        // Assert
        assertThat(domain.teamA?.name).isEqualTo("Solo Team")
        assertThat(domain.teamB).isNull()
    }

    @Test
    fun `should handle null opponent entry at index 0 without NPE`() {
        // Arrange
        val dto = fakeMatchDto().copy(
            opponents = listOf(OpponentWrapperDto(opponent = null))
        )

        // Act / Assert — must not throw
        val domain = dto.toDomain()
        assertThat(domain.teamA).isNull()
    }

    @Test
    fun `should map status string to correct MatchStatus enum`() {
        // Arrange / Act / Assert
        assertThat(fakeMatchDto(status = "running").toDomain().status).isEqualTo(MatchStatus.RUNNING)
        assertThat(fakeMatchDto(status = "not_started").toDomain().status).isEqualTo(MatchStatus.NOT_STARTED)
        assertThat(fakeMatchDto(status = "finished").toDomain().status).isEqualTo(MatchStatus.FINISHED)
        assertThat(fakeMatchDto(status = "canceled").toDomain().status).isEqualTo(MatchStatus.CANCELED)
        assertThat(fakeMatchDto(status = "postponed").toDomain().status).isEqualTo(MatchStatus.POSTPONED)
        assertThat(fakeMatchDto(status = null).toDomain().status).isEqualTo(MatchStatus.NOT_STARTED)
    }
}
