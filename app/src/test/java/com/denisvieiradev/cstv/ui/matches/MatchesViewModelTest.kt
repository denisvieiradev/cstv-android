package com.denisvieiradev.cstv.ui.matches

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.denisvieiradev.cstv.data.datasources.local.SessionRepositoryImpl
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
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

    @Test
    fun `should emit loading state when load matches starts`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

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
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

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
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

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
    fun `loadMatches success emits isAuthError false`() = runTest {
        // Arrange
        val matches = listOf(fakeMatch())
        coEvery { mockUseCase() } returns matches
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

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
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

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
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

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
    fun `confirm logout clears session and emits navigation event`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

        // Act / Assert
        viewModel.navigationEvents.test {
            viewModel.onAction(MatchesScreenAction.ConfirmLogout)
            assertThat(awaitItem()).isEqualTo(MatchesNavigationEvent.NavigateToTokenScreen)
            cancelAndConsumeRemainingEvents()
        }
        verify { mockSessionRepository.clearSession() }
    }

    @Test
    fun `configure token clears session and emits navigation event`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

        // Act / Assert
        viewModel.navigationEvents.test {
            viewModel.onAction(MatchesScreenAction.ConfigureToken)
            assertThat(awaitItem()).isEqualTo(MatchesNavigationEvent.NavigateToTokenScreen)
            cancelAndConsumeRemainingEvents()
        }
        verify { mockSessionRepository.clearSession() }
    }

    @Test
    fun `init reads saved theme preference and initializes isDarkTheme in state`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.isDarkTheme() } returns false

        // Act
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

        // Assert
        assertThat(viewModel.uiState.value.isDarkTheme).isFalse()
    }

    @Test
    fun `ToggleTheme action flips isDarkTheme and persists the new value`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.isDarkTheme() } returns false
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

        // Act
        viewModel.onAction(MatchesScreenAction.ToggleTheme)

        // Assert
        assertThat(viewModel.uiState.value.isDarkTheme).isTrue()
        verify { mockSessionRepository.saveDarkTheme(true) }
    }

    @Test
    fun `init reads saved language preference and initializes currentLanguage in state`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.getLanguage() } returns SessionRepositoryImpl.LANG_PT

        // Act
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

        // Assert
        assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(SessionRepositoryImpl.LANG_PT)
    }

    @Test
    fun `ToggleLanguage action switches from EN to PT and persists the new value`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.getLanguage() } returns SessionRepositoryImpl.LANG_EN
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

        // Act
        viewModel.onAction(MatchesScreenAction.ToggleLanguage)

        // Assert
        assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(SessionRepositoryImpl.LANG_PT)
        verify { mockSessionRepository.saveLanguage(SessionRepositoryImpl.LANG_PT) }
    }

    @Test
    fun `ToggleLanguage action switches from PT to EN and persists the new value`() = runTest {
        // Arrange
        coEvery { mockUseCase() } returns emptyList()
        every { mockSessionRepository.getLanguage() } returns SessionRepositoryImpl.LANG_PT
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

        // Act
        viewModel.onAction(MatchesScreenAction.ToggleLanguage)

        // Assert
        assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(SessionRepositoryImpl.LANG_EN)
        verify { mockSessionRepository.saveLanguage(SessionRepositoryImpl.LANG_EN) }
    }

    @Test
    fun `should reload matches when retry action is dispatched`() = runTest {
        // Arrange
        val initialMatches = listOf(fakeMatch(id = 1))
        val retryMatches = listOf(fakeMatch(id = 2))
        coEvery { mockUseCase() } returnsMany listOf(initialMatches, retryMatches)
        val viewModel = MatchesViewModel(mockUseCase, mockSessionRepository)

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
