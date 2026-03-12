package com.denisvieiradev.cstv.data.repository

import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchRemoteDataSource
import com.denisvieiradev.cstv.utils.TestConstants
import com.denisvieiradev.cstv.utils.fakeMatch
import com.denisvieiradev.cstv.utils.fakeTeam
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class MatchRepositoryImplTest {

    @RunWith(Enclosed::class)
    class GivenMatchRepository {

        class WhenGetRunningMatchesIsInvoked {

            private val mockDataSource: MatchRemoteDataSource = mockk()
            private val repository = MatchRepositoryImpl(mockDataSource)

            @Test
            fun `then it returns the list from the data source`() = runTest {
                val matches = listOf(fakeMatch(id = 1), fakeMatch(id = 2))
                coEvery { mockDataSource.getRunningMatches() } returns matches

                val result = repository.getRunningMatches()

                assertThat(result).isEqualTo(matches)
                coVerify(exactly = 1) { mockDataSource.getRunningMatches() }
            }

            @Test
            fun `then it propagates exception from the data source`() {
                val exception = RuntimeException(TestConstants.ERROR_NETWORK)
                coEvery { mockDataSource.getRunningMatches() } throws exception

                val caught = assertThrows(RuntimeException::class.java) {
                    runBlocking { repository.getRunningMatches() }
                }

                assertThat(caught.message).isEqualTo(exception.message)
            }
        }

        class WhenGetUpcomingMatchesIsInvoked {

            private val mockDataSource: MatchRemoteDataSource = mockk()
            private val repository = MatchRepositoryImpl(mockDataSource)

            @Test
            fun `then it returns the list from the data source`() = runTest {
                val matches = listOf(fakeMatch(id = 3), fakeMatch(id = 4))
                coEvery { mockDataSource.getUpcomingMatches() } returns matches

                val result = repository.getUpcomingMatches()

                assertThat(result).isEqualTo(matches)
                coVerify(exactly = 1) { mockDataSource.getUpcomingMatches() }
            }

            @Test
            fun `then it propagates exception from the data source`() {
                val exception = RuntimeException(TestConstants.ERROR_NETWORK)
                coEvery { mockDataSource.getUpcomingMatches() } throws exception

                val caught = assertThrows(RuntimeException::class.java) {
                    runBlocking { repository.getUpcomingMatches() }
                }

                assertThat(caught.message).isEqualTo(exception.message)
            }
        }

        class WhenGetMatchDetailIsInvoked {

            private val mockDataSource: MatchRemoteDataSource = mockk()
            private val repository = MatchRepositoryImpl(mockDataSource)

            @Test
            fun `then it returns the match from the data source`() = runTest {
                val match = fakeMatch(id = TestConstants.MATCH_ID)
                coEvery { mockDataSource.getMatchDetail(TestConstants.MATCH_ID) } returns match

                val result = repository.getMatchDetail(TestConstants.MATCH_ID)

                assertThat(result).isEqualTo(match)
                coVerify(exactly = 1) { mockDataSource.getMatchDetail(TestConstants.MATCH_ID) }
            }

            @Test
            fun `then it propagates exception from the data source`() {
                val exception = RuntimeException(TestConstants.ERROR_NOT_FOUND)
                coEvery { mockDataSource.getMatchDetail(any()) } throws exception

                val caught = assertThrows(RuntimeException::class.java) {
                    runBlocking { repository.getMatchDetail(TestConstants.MATCH_ID_NOT_FOUND) }
                }

                assertThat(caught.message).isEqualTo(exception.message)
            }
        }

        class WhenGetTeamWithPlayersIsInvoked {

            private val mockDataSource: MatchRemoteDataSource = mockk()
            private val repository = MatchRepositoryImpl(mockDataSource)

            @Test
            fun `then it returns the team from the data source`() = runTest {
                val team = fakeTeam(id = TestConstants.TEAM_NAVI_ID, name = TestConstants.TEAM_NAME_NAVI)
                coEvery { mockDataSource.getTeamWithPlayers(TestConstants.TEAM_NAVI_ID) } returns team

                val result = repository.getTeamWithPlayers(TestConstants.TEAM_NAVI_ID)

                assertThat(result).isEqualTo(team)
                coVerify(exactly = 1) { mockDataSource.getTeamWithPlayers(TestConstants.TEAM_NAVI_ID) }
            }

            @Test
            fun `then it propagates exception from the data source`() {
                val exception = RuntimeException(TestConstants.ERROR_TEAM_NOT_FOUND)
                coEvery { mockDataSource.getTeamWithPlayers(any()) } throws exception

                val caught = assertThrows(RuntimeException::class.java) {
                    runBlocking { repository.getTeamWithPlayers(TestConstants.MATCH_ID_NOT_FOUND) }
                }

                assertThat(caught.message).isEqualTo(exception.message)
            }
        }
    }
}
