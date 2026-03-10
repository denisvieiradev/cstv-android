package com.denisvieiradev.cstv.domain.usecase

import com.denisvieiradev.cstv.domain.repository.MatchRepository
import com.denisvieiradev.cstv.utils.fakeMatch
import com.denisvieiradev.cstv.utils.fakePlayer
import com.denisvieiradev.cstv.utils.fakeTeam
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetMatchDetailUseCaseTest {

    private val mockRepository: MatchRepository = mockk()
    private val useCase = GetMatchDetailUseCase(mockRepository)

    @Test
    fun `should return match without fallback when both teams have players`() = runTest {
        // Arrange
        val players = listOf(fakePlayer())
        val teamA = fakeTeam(id = 1, players = players)
        val teamB = fakeTeam(id = 2, players = players)
        val match = fakeMatch(teamA = teamA, teamB = teamB)
        coEvery { mockRepository.getMatchDetail(match.id) } returns match

        // Act
        val result = useCase(match.id)

        // Assert
        assertThat(result.teamA?.players).isEqualTo(players)
        assertThat(result.teamB?.players).isEqualTo(players)
        coVerify(exactly = 0) { mockRepository.getTeamWithPlayers(any()) }
    }

    @Test
    fun `should call fallback for teamA when teamA has no players`() = runTest {
        // Arrange
        val players = listOf(fakePlayer())
        val teamA = fakeTeam(id = 1, players = emptyList())
        val teamB = fakeTeam(id = 2, players = players)
        val match = fakeMatch(teamA = teamA, teamB = teamB)
        val teamAWithPlayers = fakeTeam(id = 1, players = players)
        coEvery { mockRepository.getMatchDetail(match.id) } returns match
        coEvery { mockRepository.getTeamWithPlayers(teamA.id) } returns teamAWithPlayers

        // Act
        val result = useCase(match.id)

        // Assert
        assertThat(result.teamA?.players).isEqualTo(players)
        assertThat(result.teamB?.players).isEqualTo(players)
        coVerify(exactly = 1) { mockRepository.getTeamWithPlayers(teamA.id) }
        coVerify(exactly = 0) { mockRepository.getTeamWithPlayers(teamB.id) }
    }

    @Test
    fun `should call fallback for teamB when teamB has no players`() = runTest {
        // Arrange
        val players = listOf(fakePlayer())
        val teamA = fakeTeam(id = 1, players = players)
        val teamB = fakeTeam(id = 2, players = emptyList())
        val match = fakeMatch(teamA = teamA, teamB = teamB)
        val teamBWithPlayers = fakeTeam(id = 2, players = players)
        coEvery { mockRepository.getMatchDetail(match.id) } returns match
        coEvery { mockRepository.getTeamWithPlayers(teamB.id) } returns teamBWithPlayers

        // Act
        val result = useCase(match.id)

        // Assert
        assertThat(result.teamA?.players).isEqualTo(players)
        assertThat(result.teamB?.players).isEqualTo(players)
        coVerify(exactly = 0) { mockRepository.getTeamWithPlayers(teamA.id) }
        coVerify(exactly = 1) { mockRepository.getTeamWithPlayers(teamB.id) }
    }

    @Test
    fun `should call fallback for both teams in parallel when both have no players`() = runTest {
        // Arrange
        val players = listOf(fakePlayer())
        val teamA = fakeTeam(id = 1, players = emptyList())
        val teamB = fakeTeam(id = 2, players = emptyList())
        val match = fakeMatch(teamA = teamA, teamB = teamB)
        val teamAWithPlayers = fakeTeam(id = 1, players = players)
        val teamBWithPlayers = fakeTeam(id = 2, players = players)
        coEvery { mockRepository.getMatchDetail(match.id) } returns match
        coEvery { mockRepository.getTeamWithPlayers(teamA.id) } returns teamAWithPlayers
        coEvery { mockRepository.getTeamWithPlayers(teamB.id) } returns teamBWithPlayers

        // Act
        val result = useCase(match.id)

        // Assert
        assertThat(result.teamA?.players).isEqualTo(players)
        assertThat(result.teamB?.players).isEqualTo(players)
        coVerify(exactly = 1) { mockRepository.getTeamWithPlayers(teamA.id) }
        coVerify(exactly = 1) { mockRepository.getTeamWithPlayers(teamB.id) }
    }

    @Test
    fun `should propagate exception when getMatchDetail fails`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { mockRepository.getMatchDetail(any()) } throws exception

        // Act
        var caughtException: Exception? = null
        try {
            useCase(matchId = 1)
        } catch (e: Exception) {
            caughtException = e
        }

        // Assert
        assertThat(caughtException).isInstanceOf(RuntimeException::class.java)
        assertThat(caughtException?.message).isEqualTo(exception.message)
    }
}
