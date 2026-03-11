package com.denisvieiradev.cstv.ui.token

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class TokenViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mockSessionLocalDataSource: SessionLocalDataSource = mockk(relaxed = true)
    private val mockDemoSessionManager: DemoSessionManager = mockk(relaxed = true)

    private fun createViewModel() = TokenViewModel(
        mockSessionLocalDataSource,
        mockDemoSessionManager,
        mainDispatcherRule.testDispatcher
    )

    @Test
    fun `should disable confirm button when token is blank`() {
        // Arrange
        val viewModel = createViewModel()

        // Act / Assert
        assertThat(viewModel.uiState.value.isConfirmEnabled).isFalse()
    }

    @Test
    fun `should enable confirm button when token is not blank`() {
        // Arrange
        val viewModel = createViewModel()

        // Act
        viewModel.onAction(TokenScreenAction.OnTokenChanged("my-token"))

        // Assert
        assertThat(viewModel.uiState.value.isConfirmEnabled).isTrue()
    }

    @Test
    fun `should save token when confirm action is dispatched`() = runTest {
        // Arrange
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged("my-token"))

        // Act
        viewModel.onAction(TokenScreenAction.Confirm)
        advanceUntilIdle()

        // Assert
        verify { mockSessionLocalDataSource.saveToken("my-token") }
        assertThat(viewModel.uiState.value.navigateToMatches).isTrue()
    }

    @Test
    fun `should not navigate when submitting blank token`() {
        // Arrange
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged("   "))

        // Act
        viewModel.onAction(TokenScreenAction.Confirm)

        // Assert
        assertThat(viewModel.uiState.value.navigateToMatches).isFalse()
    }

    @Test
    fun `should reset navigateToMatches after onNavigationConsumed`() = runTest {
        // Arrange
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged("my-token"))
        viewModel.onAction(TokenScreenAction.Confirm)
        assertThat(viewModel.uiState.value.navigateToMatches).isTrue()

        // Act
        viewModel.onNavigationConsumed()

        // Assert
        assertThat(viewModel.uiState.value.navigateToMatches).isFalse()
    }

    @Test
    fun `should emit error state when save token fails`() = runTest {
        // Arrange
        val exception = RuntimeException("Save failed")
        every { mockSessionLocalDataSource.saveToken(any()) } throws exception
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged("my-token"))

        // Act
        viewModel.onAction(TokenScreenAction.Confirm)
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value.error).isEqualTo(exception)
    }

    @Test
    fun `should flip isDarkTheme and persist new value when ToggleTheme action is dispatched`() = runTest {
        // Arrange
        every { mockSessionLocalDataSource.isDarkTheme() } returns false
        val viewModel = createViewModel()

        // Act
        viewModel.onAction(TokenScreenAction.ToggleTheme)
        advanceUntilIdle()

        // Assert
        assertThat(viewModel.uiState.value.isDarkTheme).isTrue()
        verify { mockSessionLocalDataSource.saveDarkTheme(true) }
    }

    @Test
    fun `should switch language from EN to PT and emit RecreateActivity when ToggleLanguage action is dispatched`() = runTest {
        // Arrange
        every { mockSessionLocalDataSource.getLanguage() } returns Language.EN
        val viewModel = createViewModel()

        // Act / Assert
        viewModel.navigationEvents.test {
            viewModel.onAction(TokenScreenAction.ToggleLanguage)
            assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(Language.PT)
            assertThat(awaitItem()).isEqualTo(TokenNavigationEvent.RecreateActivity)
            cancelAndConsumeRemainingEvents()
        }
        verify { mockSessionLocalDataSource.saveLanguage(Language.PT) }
    }

    @Test
    fun `should set showTutorialDialog to true when ShowTutorial action is dispatched`() {
        // Arrange
        val viewModel = createViewModel()

        // Act
        viewModel.onAction(TokenScreenAction.ShowTutorial)

        // Assert
        assertThat(viewModel.uiState.value.showTutorialDialog).isTrue()
    }

    @Test
    fun `should set showTutorialDialog to false when DismissTutorial action is dispatched`() {
        // Arrange
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.ShowTutorial)
        assertThat(viewModel.uiState.value.showTutorialDialog).isTrue()

        // Act
        viewModel.onAction(TokenScreenAction.DismissTutorial)

        // Assert
        assertThat(viewModel.uiState.value.showTutorialDialog).isFalse()
    }

    @Test
    fun `should start demo and navigate to matches when TryDemo action is dispatched`() {
        // Arrange
        val viewModel = createViewModel()

        // Act
        viewModel.onAction(TokenScreenAction.TryDemo)

        // Assert
        verify { mockDemoSessionManager.startDemo() }
        assertThat(viewModel.uiState.value.navigateToMatches).isTrue()
    }

    @Test
    fun `should set token and close tutorial when PasteTokenFromClipboard is dispatched with text`() {
        // Arrange
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.ShowTutorial)

        // Act
        viewModel.onAction(TokenScreenAction.PasteTokenFromClipboard("abc123"))

        // Assert
        assertThat(viewModel.uiState.value.token).isEqualTo("abc123")
        assertThat(viewModel.uiState.value.showTutorialDialog).isFalse()
    }

    @Test
    fun `should keep token unchanged and close tutorial when PasteTokenFromClipboard is dispatched with null`() {
        // Arrange
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged("existing-token"))
        viewModel.onAction(TokenScreenAction.ShowTutorial)

        // Act
        viewModel.onAction(TokenScreenAction.PasteTokenFromClipboard(null))

        // Assert
        assertThat(viewModel.uiState.value.token).isEqualTo("existing-token")
        assertThat(viewModel.uiState.value.showTutorialDialog).isFalse()
    }

    @Test
    fun `should trim whitespace from clipboard text when PasteTokenFromClipboard is dispatched with spaces`() {
        // Arrange
        val viewModel = createViewModel()

        // Act
        viewModel.onAction(TokenScreenAction.PasteTokenFromClipboard("  abc  "))

        // Assert
        assertThat(viewModel.uiState.value.token).isEqualTo("abc")
    }
}
