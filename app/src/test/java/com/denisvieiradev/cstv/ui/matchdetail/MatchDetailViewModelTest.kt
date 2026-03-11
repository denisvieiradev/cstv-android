package com.denisvieiradev.cstv.ui.matchdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.usecase.GetMatchDetailUseCase
import com.denisvieiradev.cstv.ui.matchdetail.model.MatchDetailNavigationEvent
import com.denisvieiradev.cstv.ui.matchdetail.model.MatchDetailScreenAction
import com.denisvieiradev.cstv.ui.matchdetail.model.PlayersState
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.denisvieiradev.cstv.utils.TestConstants
import com.denisvieiradev.cstv.utils.fakeMatch
import com.denisvieiradev.cstv.utils.fakePlayer
import com.denisvieiradev.cstv.utils.fakeTeam
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class MatchDetailViewModelTest {

    @RunWith(Enclosed::class)
    class GivenMatchInSavedStateHandle {

        class WhenViewModelIsCreated : MatchDetailViewModelTestBase() {

            @Test
            fun `then uiState match is populated`() = runTest {
                val match = fakeMatch(id = TestConstants.MATCH_ID)
                coEvery { mockGetMatchDetailUseCase(TestConstants.MATCH_ID) } returns match
                val viewModel = createViewModel(match)

                assertThat(viewModel.uiState.value.match).isEqualTo(match)
            }

            @Test
            fun `then playersState is Loading`() = runTest {
                val match = fakeMatch(id = TestConstants.MATCH_ID_DETAIL)
                coEvery { mockGetMatchDetailUseCase(TestConstants.MATCH_ID_DETAIL) } returns match
                val viewModel = createViewModel(match)

                viewModel.uiState.test {
                    assertThat(awaitItem().playersState).isEqualTo(PlayersState.Loading)
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then darkTheme is read from session`() = runTest {
                every { mockSessionLocalDataSource.isDarkTheme() } returns true
                val match = fakeMatch(id = TestConstants.MATCH_ID_DETAIL)
                coEvery { mockGetMatchDetailUseCase(TestConstants.MATCH_ID_DETAIL) } returns match
                val viewModel = createViewModel(match)

                assertThat(viewModel.uiState.value.darkTheme).isTrue()
                verify { mockSessionLocalDataSource.isDarkTheme() }
            }
        }

        class WhenFetchPlayersSucceeds : MatchDetailViewModelTestBase() {

            @Test
            fun `then playersState is Success with teams`() = runTest {
                val teamA = fakeTeam(id = TestConstants.TEAM_A_ID, players = listOf(fakePlayer()))
                val teamB = fakeTeam(id = TestConstants.TEAM_B_ID, players = listOf(fakePlayer(id = 2, name = "NiKo")))
                val detailMatch = fakeMatch(id = 10, teamA = teamA, teamB = teamB)
                coEvery { mockGetMatchDetailUseCase(TestConstants.MATCH_ID_DETAIL) } returns detailMatch
                val viewModel = createViewModel(fakeMatch(id = TestConstants.MATCH_ID_DETAIL))

                viewModel.uiState.test {
                    awaitItem() // initial state (Loading)
                    assertThat(awaitItem().playersState).isEqualTo(
                        PlayersState.Success(teamA = teamA.players, teamB = teamB.players)
                    )
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        class WhenFetchPlayersFails : MatchDetailViewModelTestBase() {

            @Test
            fun `then playersState is Error`() = runTest {
                val match = fakeMatch(id = TestConstants.MATCH_ID_DETAIL)
                coEvery { mockGetMatchDetailUseCase(TestConstants.MATCH_ID_DETAIL) } throws RuntimeException("Network error")
                val viewModel = createViewModel(match)

                viewModel.uiState.test {
                    awaitItem() // initial state (Loading)
                    assertThat(awaitItem().playersState).isEqualTo(PlayersState.Error)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        class WhenRetryLoadPlayersIsDispatched : MatchDetailViewModelTestBase() {

            @Test
            fun `then playersState transitions Loading then Success`() = runTest {
                val match = fakeMatch(id = TestConstants.MATCH_ID_DETAIL)
                val teamA = fakeTeam(id = TestConstants.TEAM_A_ID, players = listOf(fakePlayer()))
                val teamB = fakeTeam(id = TestConstants.TEAM_B_ID, players = emptyList())
                val detailMatch = fakeMatch(id = 10, teamA = teamA, teamB = teamB)
                coEvery { mockGetMatchDetailUseCase(TestConstants.MATCH_ID_DETAIL) } throws RuntimeException("error") andThen detailMatch
                val viewModel = createViewModel(match)

                viewModel.uiState.test {
                    awaitItem() // initial Loading
                    awaitItem() // Error after first fetch

                    viewModel.onAction(MatchDetailScreenAction.RetryLoadPlayers)

                    assertThat(awaitItem().playersState).isEqualTo(PlayersState.Loading)
                    assertThat(awaitItem().playersState).isEqualTo(
                        PlayersState.Success(teamA = teamA.players, teamB = teamB.players)
                    )
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenNoMatchInSavedStateHandle {

        class WhenViewModelIsCreated : MatchDetailViewModelTestBase() {

            @Test
            fun `then uiState match is null`() = runTest {
                val viewModel = createViewModel()

                assertThat(viewModel.uiState.value.match).isNull()
            }

            @Test
            fun `then playersState is Idle`() = runTest {
                val viewModel = createViewModel()

                assertThat(viewModel.uiState.value.playersState).isEqualTo(PlayersState.Idle)
            }

            @Test
            fun `then use case is not called`() = runTest {
                createViewModel()

                coVerify(exactly = 0) { mockGetMatchDetailUseCase(any()) }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenViewModelIsCreated {

        class WhenNavigateBackIsDispatched : MatchDetailViewModelTestBase() {

            @Test
            fun `then NavigateBack event is emitted`() = runTest {
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchDetailScreenAction.NavigateBack)
                    assertThat(awaitItem()).isEqualTo(MatchDetailNavigationEvent.NavigateBack)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }
}

abstract class MatchDetailViewModelTestBase {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val mockSessionLocalDataSource: SessionLocalDataSource = mockk(relaxed = true)
    val mockGetMatchDetailUseCase: GetMatchDetailUseCase = mockk()

    fun createViewModel(match: Match? = null) = MatchDetailViewModel(
        savedStateHandle = SavedStateHandle(
            if (match != null) mapOf(MatchDetailViewModel.EXTRA_MATCH to match) else emptyMap()
        ),
        sessionLocalDataSource = mockSessionLocalDataSource,
        getMatchDetailUseCase = mockGetMatchDetailUseCase
    )
}
