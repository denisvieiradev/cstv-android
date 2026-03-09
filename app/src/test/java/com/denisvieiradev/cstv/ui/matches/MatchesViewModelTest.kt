package com.denisvieiradev.cstv.ui.matches

import app.cash.turbine.test
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.denisvieiradev.cstv.utils.fakeMatch
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MatchesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockUseCase: GetCsMatchesUseCase = mockk()

    @Test
    fun `should emit loading state when load matches starts`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase)

        // Act / Assert
        viewModel.uiState.test {
            awaitItem() // initial state
            assertThat(awaitItem().isLoading).isTrue()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should emit matches list when use case succeeds`() = runTest {
        // Arrange
        val matches = listOf(fakeMatch())
        coEvery { mockUseCase() } returns matches
        val viewModel = MatchesViewModel(mockUseCase)

        // Act / Assert
        viewModel.uiState.test {
            awaitItem() // initial state
            awaitItem() // loading state
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.matches).isEqualTo(matches)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should emit error state when use case throws exception`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { mockUseCase() } throws exception
        val viewModel = MatchesViewModel(mockUseCase)

        // Act / Assert
        viewModel.uiState.test {
            awaitItem() // initial state
            awaitItem() // loading state
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo(exception)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should reload matches when retry action is dispatched`() = runTest {
        // Arrange
        val initialMatches = listOf(fakeMatch(id = 1))
        val retryMatches = listOf(fakeMatch(id = 2))
        coEvery { mockUseCase() } returnsMany listOf(initialMatches, retryMatches)
        val viewModel = MatchesViewModel(mockUseCase)

        // Act / Assert
        viewModel.uiState.test {
            awaitItem() // initial state
            awaitItem() // loading from init
            awaitItem() // result from init

            viewModel.onAction(MatchesScreenAction.Retry)

            assertThat(awaitItem().isLoading).isTrue()
            val retryResult = awaitItem()
            assertThat(retryResult.matches).isEqualTo(retryMatches)
            cancelAndConsumeRemainingEvents()
        }
    }
}
