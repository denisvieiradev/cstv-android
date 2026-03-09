package com.denisvieiradev.cstv.domain.usecase

import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.domain.repository.MatchRepository
import com.denisvieiradev.cstv.utils.fakeMatch
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetCsMatchesUseCaseTest {

    private val mockRepository: MatchRepository = mockk()
    private val useCase = GetCsMatchesUseCase(mockRepository)

    @Test
    fun `should return running matches before upcoming matches`() = runTest {
        // Arrange
        val runningMatches = listOf(fakeMatch(id = 1, status = MatchStatus.RUNNING))
        val upcomingMatches = listOf(fakeMatch(id = 2, status = MatchStatus.NOT_STARTED))
        coEvery { mockRepository.getRunningMatches() } returns runningMatches
        coEvery { mockRepository.getUpcomingMatches() } returns upcomingMatches

        // Act
        val result = useCase()

        // Assert
        assertThat(result).hasSize(2)
        assertThat(result[0]).isEqualTo(runningMatches[0])
        assertThat(result[1]).isEqualTo(upcomingMatches[0])
    }

    @Test
    fun `should return empty list when both endpoints return empty`() = runTest {
        // Arrange
        coEvery { mockRepository.getRunningMatches() } returns emptyList()
        coEvery { mockRepository.getUpcomingMatches() } returns emptyList()

        // Act
        val result = useCase()

        // Assert
        assertThat(result).isEmpty()
    }

    @Test
    fun `should propagate exception when repository throws`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { mockRepository.getRunningMatches() } throws exception

        // Act
        var caughtException: Exception? = null
        try {
            useCase()
        } catch (e: Exception) {
            caughtException = e
        }

        // Assert
        assertThat(caughtException).isEqualTo(exception)
    }
}
