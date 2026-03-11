package com.denisvieiradev.cstv.ui.matchdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.domain.model.Match
import com.denisvieiradev.cstv.domain.usecase.GetMatchDetailUseCase
import com.denisvieiradev.cstv.utils.MainDispatcherRule
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

class MatchDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockSessionLocalDataSource: SessionLocalDataSource = mockk(relaxed = true)
    private val mockGetMatchDetailUseCase: GetMatchDetailUseCase = mockk()

    private fun buildViewModel(match: Match? = null) = MatchDetailViewModel(
        savedStateHandle = SavedStateHandle(
            if (match != null) mapOf(MatchDetailViewModel.EXTRA_MATCH to match) else emptyMap()
        ),
        sessionLocalDataSource = mockSessionLocalDataSource,
        getMatchDetailUseCase = mockGetMatchDetailUseCase
    )

    @Test
    fun `init with match in SavedStateHandle populates uiState match correctly`() = runTest {
        val match = fakeMatch(id = 42)
        coEvery { mockGetMatchDetailUseCase(42) } returns match

        val viewModel = buildViewModel(match)

        assertThat(viewModel.uiState.value.match).isEqualTo(match)
    }

    @Test
    fun `init with empty SavedStateHandle leaves uiState match as null`() = runTest {
        val viewModel = buildViewModel()

        assertThat(viewModel.uiState.value.match).isNull()
    }

    @Test
    fun `onAction NavigateBack emits MatchDetailNavigationEvent NavigateBack`() = runTest {
        val viewModel = buildViewModel()

        viewModel.navigationEvents.test {
            viewModel.onAction(MatchDetailScreenAction.NavigateBack)
            assertThat(awaitItem()).isEqualTo(MatchDetailNavigationEvent.NavigateBack)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init reads darkTheme from sessionRepository and sets it in uiState`() = runTest {
        every { mockSessionLocalDataSource.isDarkTheme() } returns true
        val viewModel = buildViewModel()

        assertThat(viewModel.uiState.value.darkTheme).isTrue()
        verify { mockSessionLocalDataSource.isDarkTheme() }
    }

    @Test
    fun `init with match sets playersState to Loading before fetch completes`() = runTest {
        val match = fakeMatch(id = 10)
        coEvery { mockGetMatchDetailUseCase(10) } returns match
        val viewModel = buildViewModel(match)

        viewModel.uiState.test {
            assertThat(awaitItem().playersState).isEqualTo(PlayersState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchPlayers sets playersState to Success when use case succeeds`() = runTest {
        val teamA = fakeTeam(id = 1, players = listOf(fakePlayer()))
        val teamB = fakeTeam(id = 2, players = listOf(fakePlayer(id = 2, name = "NiKo")))
        val detailMatch = fakeMatch(id = 10, teamA = teamA, teamB = teamB)
        coEvery { mockGetMatchDetailUseCase(10) } returns detailMatch
        val viewModel = buildViewModel(fakeMatch(id = 10))

        viewModel.uiState.test {
            awaitItem() // initial state (Loading)
            assertThat(awaitItem().playersState).isEqualTo(
                PlayersState.Success(teamA = teamA.players, teamB = teamB.players)
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchPlayers sets playersState to Error when use case throws`() = runTest {
        val match = fakeMatch(id = 10)
        coEvery { mockGetMatchDetailUseCase(10) } throws RuntimeException("Network error")
        val viewModel = buildViewModel(match)

        viewModel.uiState.test {
            awaitItem() // initial state (Loading)
            assertThat(awaitItem().playersState).isEqualTo(PlayersState.Error)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction RetryLoadPlayers re-triggers fetch transitioning Loading then Success`() = runTest {
        val match = fakeMatch(id = 10)
        val teamA = fakeTeam(id = 1, players = listOf(fakePlayer()))
        val teamB = fakeTeam(id = 2, players = emptyList())
        val detailMatch = fakeMatch(id = 10, teamA = teamA, teamB = teamB)
        coEvery { mockGetMatchDetailUseCase(10) } throws RuntimeException("error") andThen detailMatch
        val viewModel = buildViewModel(match)

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

    @Test
    fun `init with null match does not trigger fetchPlayers and playersState is Idle`() = runTest {
        val viewModel = buildViewModel()

        assertThat(viewModel.uiState.value.playersState).isEqualTo(PlayersState.Idle)
        coVerify(exactly = 0) { mockGetMatchDetailUseCase(any()) }
    }
}
