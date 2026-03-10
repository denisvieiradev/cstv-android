package com.denisvieiradev.cstv.ui.matches

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import com.denisvieiradev.cstv.ui.matchdetail.SelectedMatchHolder
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.denisvieiradev.cstv.utils.fakeMatch
import com.denisvieiradev.network.data.remote.utils.AuthorizationException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MatchesViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockUseCase: GetCsMatchesUseCase = mockk()
    private val mockSessionRepository: SessionRepository = mockk(relaxed = true)
    private val selectedMatchHolder = SelectedMatchHolder()

    @Test
    fun `should emit loading state when load matches starts`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

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
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

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
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

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
    fun `should emit isAuthError false when use case succeeds`() = runTest {
        // Arrange
        val matches = listOf(fakeMatch())
        coEvery { mockUseCase() } returns matches
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act / Assert
        viewModel.uiState.test {
            awaitItem() // initial state
            awaitItem() // loading state
            val successState = awaitItem()
            assertThat(successState.isLoading).isFalse()
            assertThat(successState.matches).isEqualTo(matches)
            assertThat(successState.isAuthError).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `loadMatches with AuthorizationException sets isAuthError true and error null`() = runTest {
        // Arrange
        coEvery { mockUseCase() } throws AuthorizationException(401)
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act / Assert
        viewModel.uiState.test {
            awaitItem() // initial state
            awaitItem() // loading state
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.isAuthError).isTrue()
            assertThat(errorState.error).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `loadMatches with generic exception sets error and isAuthError false`() = runTest {
        // Arrange
        val exception = RuntimeException("Network error")
        coEvery { mockUseCase() } throws exception
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act / Assert
        viewModel.uiState.test {
            awaitItem() // initial state
            awaitItem() // loading state
            val errorState = awaitItem()
            assertThat(errorState.isLoading).isFalse()
            assertThat(errorState.error).isEqualTo(exception)
            assertThat(errorState.isAuthError).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should clear session and emit NavigateToTokenScreen when ConfirmLogout action is dispatched`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act / Assert
        viewModel.navigationEvents.test {
            viewModel.onAction(MatchesScreenAction.ConfirmLogout)
            assertThat(awaitItem()).isEqualTo(MatchesNavigationEvent.NavigateToTokenScreen)
            cancelAndConsumeRemainingEvents()
        }
        verify { mockSessionRepository.clearSession() }
    }

    @Test
    fun `should clear session and emit NavigateToTokenScreen when ConfigureToken action is dispatched`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act / Assert
        viewModel.navigationEvents.test {
            viewModel.onAction(MatchesScreenAction.ConfigureToken)
            assertThat(awaitItem()).isEqualTo(MatchesNavigationEvent.NavigateToTokenScreen)
            cancelAndConsumeRemainingEvents()
        }
        verify { mockSessionRepository.clearSession() }
    }

    @Test
    fun `should initialise isDarkTheme from saved preference on creation`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.isDarkTheme() } returns false

        // Act
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Assert
        assertThat(viewModel.uiState.value.isDarkTheme).isFalse()
    }

    @Test
    fun `should flip isDarkTheme and persist new value when ToggleTheme action is dispatched`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.isDarkTheme() } returns false
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act
        viewModel.onAction(MatchesScreenAction.ToggleTheme)

        // Assert
        assertThat(viewModel.uiState.value.isDarkTheme).isTrue()
        verify { mockSessionRepository.saveDarkTheme(true) }
    }

    @Test
    fun `should initialise currentLanguage from saved preference on creation`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.getLanguage() } returns Language.PT

        // Act
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Assert
        assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(Language.PT)
    }

    @Test
    fun `should switch language from EN to PT and persist when ToggleLanguage action is dispatched`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.getLanguage() } returns Language.EN
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act
        viewModel.onAction(MatchesScreenAction.ToggleLanguage)

        // Assert
        assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(Language.PT)
        verify { mockSessionRepository.saveLanguage(Language.PT) }
    }

    @Test
    fun `should switch language from PT to EN and persist when ToggleLanguage action is dispatched`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.getLanguage() } returns Language.PT
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act
        viewModel.onAction(MatchesScreenAction.ToggleLanguage)

        // Assert
        assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(Language.EN)
        verify { mockSessionRepository.saveLanguage(Language.EN) }
    }

    @Test
    fun `should set showLogoutDialog to true when Logout action is dispatched`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

        // Act
        viewModel.onAction(MatchesScreenAction.Logout)

        // Assert
        assertThat(viewModel.uiState.value.showLogoutDialog).isTrue()
    }

    @Test
    fun `should set showLogoutDialog to false when DismissLogout action is dispatched`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)
        viewModel.onAction(MatchesScreenAction.Logout)
        assertThat(viewModel.uiState.value.showLogoutDialog).isTrue()

        // Act
        viewModel.onAction(MatchesScreenAction.DismissLogout)

        // Assert
        assertThat(viewModel.uiState.value.showLogoutDialog).isFalse()
    }

    @Test
    fun `should reload matches when retry action is dispatched`() = runTest {
        // Arrange
        val initialMatches = listOf(fakeMatch(id = 1))
        val retryMatches = listOf(fakeMatch(id = 2))
        coEvery { mockUseCase() } returnsMany listOf(initialMatches, retryMatches)
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository, selectedMatchHolder)

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
