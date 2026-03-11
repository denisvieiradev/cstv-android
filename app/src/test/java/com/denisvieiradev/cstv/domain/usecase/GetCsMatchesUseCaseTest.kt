package com.denisvieiradev.cstv.domain.usecase

import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.domain.repository.MatchRepository
import com.denisvieiradev.cstv.utils.fakeMatch
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class GetCsMatchesUseCaseTest {

    @RunWith(Enclosed::class)
    class GivenBothEndpointsReturnResults {

        class WhenUseCaseIsInvoked {

            private val mockRepository: MatchRepository = mockk()
            private val useCase = GetCsMatchesUseCase(mockRepository)

            @Test
            fun `then running matches appear before upcoming matches`() = runTest {
                val runningMatches = listOf(fakeMatch(id = 1, status = MatchStatus.RUNNING))
                val upcomingMatches = listOf(fakeMatch(id = 2, status = MatchStatus.NOT_STARTED))
                coEvery { mockRepository.getRunningMatches() } returns runningMatches
                coEvery { mockRepository.getUpcomingMatches() } returns upcomingMatches

                val result = useCase()

                assertThat(result[0]).isEqualTo(runningMatches[0])
                assertThat(result[1]).isEqualTo(upcomingMatches[0])
            }

            @Test
            fun `then result size is sum of both lists`() = runTest {
                val runningMatches = listOf(fakeMatch(id = 1), fakeMatch(id = 2))
                val upcomingMatches = listOf(fakeMatch(id = 3), fakeMatch(id = 4), fakeMatch(id = 5))
                coEvery { mockRepository.getRunningMatches() } returns runningMatches
                coEvery { mockRepository.getUpcomingMatches() } returns upcomingMatches

                val result = useCase()

                assertThat(result).hasSize(runningMatches.size + upcomingMatches.size)
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenBothEndpointsReturnEmpty {

        class WhenUseCaseIsInvoked {

            private val mockRepository: MatchRepository = mockk()
            private val useCase = GetCsMatchesUseCase(mockRepository)

            @Test
            fun `then the result is an empty list`() = runTest {
                coEvery { mockRepository.getRunningMatches() } returns emptyList()
                coEvery { mockRepository.getUpcomingMatches() } returns emptyList()

                val result = useCase()

                assertThat(result).isEmpty()
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenRunningEndpointThrows {

        class WhenUseCaseIsInvoked {

            private val mockRepository: MatchRepository = mockk()
            private val useCase = GetCsMatchesUseCase(mockRepository)

            @Test
            fun `then the exception propagates to the caller`() = runTest {
                val exception = RuntimeException("Network error")
                coEvery { mockRepository.getRunningMatches() } throws exception
                coEvery { mockRepository.getUpcomingMatches() } returns emptyList()

                var caughtException: Exception? = null
                try {
                    useCase()
                } catch (e: Exception) {
                    caughtException = e
                }

                assertThat(caughtException).isInstanceOf(RuntimeException::class.java)
                assertThat(caughtException?.message).isEqualTo(exception.message)
            }
        }
    }
}
