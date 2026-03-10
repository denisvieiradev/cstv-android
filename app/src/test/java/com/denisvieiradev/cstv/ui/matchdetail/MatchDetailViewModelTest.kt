package com.denisvieiradev.cstv.ui.matchdetail

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.denisvieiradev.cstv.utils.fakeMatch
import com.google.common.truth.Truth.assertThat
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

    @Test
    fun `init with match in holder populates uiState match correctly`() = runTest {
        // Arrange
        val match = fakeMatch(id = 42)
        val holder = SelectedMatchHolder().apply { set(match) }
        val viewModel = MatchDetailViewModel(holder, mockSessionRepository)

        // Assert
        assertThat(viewModel.uiState.value.match).isEqualTo(match)
    }

    @Test
    fun `init calls clear on holder after consuming match`() = runTest {
        // Arrange
        val match = fakeMatch()
        val holder = SelectedMatchHolder().apply { set(match) }

        // Act
        MatchDetailViewModel(holder, mockSessionRepository)

        // Assert
        assertThat(holder.get()).isNull()
    }

    @Test
    fun `init with empty holder leaves uiState match as null`() = runTest {
        // Arrange
        val holder = SelectedMatchHolder()
        val viewModel = MatchDetailViewModel(holder, mockSessionRepository)

        // Assert
        assertThat(viewModel.uiState.value.match).isNull()
    }

    @Test
    fun `onAction NavigateBack emits MatchDetailNavigationEvent NavigateBack`() = runTest {
        // Arrange
        val holder = SelectedMatchHolder()
        val viewModel = MatchDetailViewModel(holder, mockSessionRepository)

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
        val viewModel = MatchDetailViewModel(holder, mockSessionRepository)

        // Assert
        assertThat(viewModel.uiState.value.darkTheme).isTrue()
        verify { mockSessionRepository.isDarkTheme() }
    }
}
