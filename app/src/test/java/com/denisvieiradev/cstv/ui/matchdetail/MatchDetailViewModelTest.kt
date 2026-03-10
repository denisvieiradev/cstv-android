package com.denisvieiradev.cstv.ui.matchdetail

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
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

    private val mockSessionRepository: SessionRepository = mockk(relaxed = true)
    private val mockGetMatchDetailUseCase: GetMatchDetailUseCase = mockk()

    private fun buildViewModel(holder: SelectedMatchHolder = SelectedMatchHolder()) =
        MatchDetailViewModel(holder, mockSessionRepository, mockGetMatchDetailUseCase)

    @Test
    fun `init with match in holder populates uiState match correctly`() = runTest {
        // Arrange
        val match = fakeMatch(id = 42)
        val holder = SelectedMatchHolder().apply { set(match) }
        coEvery { mockGetMatchDetailUseCase(42) } returns match

        // Act
        val viewModel = buildViewModel(holder)

        // Assert
        assertThat(viewModel.uiState.value.match).isEqualTo(match)
    }

    @Test
    fun `init calls clear on holder after consuming match`() = runTest {
        // Arrange
        val match = fakeMatch()
        val holder = SelectedMatchHolder().apply { set(match) }
        coEvery { mockGetMatchDetailUseCase(any()) } returns match

        // Act
        buildViewModel(holder)

        // Assert
        assertThat(holder.get()).isNull()
    }

    @Test
    fun `init with empty holder leaves uiState match as null`() = runTest {
        // Arrange
        val holder = SelectedMatchHolder()
        val viewModel = buildViewModel(holder)

        // Assert
        assertThat(viewModel.uiState.value.match).isNull()
    }

    @Test
    fun `onAction NavigateBack emits MatchDetailNavigationEvent NavigateBack`() = runTest {
        // Arrange
        val holder = SelectedMatchHolder()
        val viewModel = buildViewModel(holder)

        // Act / Assert
        viewModel.navigationEvents.test {
            viewModel.onAction(MatchDetailScreenAction.NavigateBack)
            assertThat(awaitItem()).isEqualTo(MatchDetailNavigationEvent.NavigateBack)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init reads darkTheme from sessionRepository and sets it in uiState`() = runTest {
        // Arrange
        every { mockSessionRepository.isDarkTheme() } returns true
        val holder = SelectedMatchHolder()
        val viewModel = buildViewModel(holder)

        // Assert
        assertThat(viewModel.uiState.value.darkTheme).isTrue()
        verify { mockSessionRepository.isDarkTheme() }
    }

    @Test
    fun `init with match sets playersState to Loading before fetch completes`() = runTest {
        // Arrange
        val match = fakeMatch(id = 10)
        val holder = SelectedMatchHolder().apply { set(match) }
        coEvery { mockGetMatchDetailUseCase(10) } returns match
        val viewModel = buildViewModel(holder)

        // Act / Assert
        viewModel.uiState.test {
            assertThat(awaitItem().playersState).isEqualTo(PlayersState.Loading)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `fetchPlayers sets playersState to Success when use case succeeds`() = runTest {
        // Arrange
        val teamA = fakeTeam(id = 1, players = listOf(fakePlayer()))
        val teamB = fakeTeam(id = 2, players = listOf(fakePlayer(id = 2, name = "NiKo")))
        val detailMatch = fakeMatch(id = 10, teamA = teamA, teamB = teamB)
        val holder = SelectedMatchHolder().apply { set(fakeMatch(id = 10)) }
        coEvery { mockGetMatchDetailUseCase(10) } returns detailMatch
        val viewModel = buildViewModel(holder)

        // Act / Assert
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
        // Arrange
        val match = fakeMatch(id = 10)
        val holder = SelectedMatchHolder().apply { set(match) }
        coEvery { mockGetMatchDetailUseCase(10) } throws RuntimeException("Network error")
        val viewModel = buildViewModel(holder)

        // Act / Assert
        viewModel.uiState.test {
            awaitItem() // initial state (Loading)
            assertThat(awaitItem().playersState).isEqualTo(PlayersState.Error)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onAction RetryLoadPlayers re-triggers fetch transitioning Loading then Success`() = runTest {
        // Arrange
        val match = fakeMatch(id = 10)
        val holder = SelectedMatchHolder().apply { set(match) }
        val teamA = fakeTeam(id = 1, players = listOf(fakePlayer()))
        val teamB = fakeTeam(id = 2, players = emptyList())
        val detailMatch = fakeMatch(id = 10, teamA = teamA, teamB = teamB)
        coEvery { mockGetMatchDetailUseCase(10) } throws RuntimeException("error") andThen detailMatch
        val viewModel = buildViewModel(holder)

        // Act / Assert
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
    fun `init with null match does not trigger fetchPlayers and playersState remains Loading`() = runTest {
        // Arrange
        val holder = SelectedMatchHolder()
        val viewModel = buildViewModel(holder)

        // Assert
        assertThat(viewModel.uiState.value.playersState).isEqualTo(PlayersState.Loading)
        coVerify(exactly = 0) { mockGetMatchDetailUseCase(any()) }
    }
}
