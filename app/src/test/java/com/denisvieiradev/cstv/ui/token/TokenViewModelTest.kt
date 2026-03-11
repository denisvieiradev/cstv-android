package com.denisvieiradev.cstv.ui.token

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.ui.core.LocaleManager
import com.denisvieiradev.cstv.ui.core.ThemeManager
import com.denisvieiradev.cstv.ui.token.model.TokenNavigationEvent
import com.denisvieiradev.cstv.ui.token.model.TokenScreenAction
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.denisvieiradev.cstv.utils.TestConstants
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
    private val mockThemeManager: ThemeManager = mockk(relaxed = true)
    private val mockLocaleManager: LocaleManager = mockk(relaxed = true)

    private fun createViewModel() = TokenViewModel(
        mockSessionLocalDataSource,
        mockDemoSessionManager,
        mockThemeManager,
        mockLocaleManager,
        mainDispatcherRule.testDispatcher
    )

    @Test
    fun `should disable confirm button when token is blank`() {
        val viewModel = createViewModel()

        assertThat(viewModel.uiState.value.isConfirmEnabled).isFalse()
    }

    @Test
    fun `should enable confirm button when token is not blank`() {
        val viewModel = createViewModel()

        viewModel.onAction(TokenScreenAction.OnTokenChanged(TestConstants.TOKEN))

        assertThat(viewModel.uiState.value.isConfirmEnabled).isTrue()
    }

    @Test
    fun `should save token when confirm action is dispatched`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged(TestConstants.TOKEN))

        viewModel.onAction(TokenScreenAction.Confirm)
        advanceUntilIdle()

        verify { mockSessionLocalDataSource.saveToken(TestConstants.TOKEN) }
        assertThat(viewModel.uiState.value.navigateToMatches).isTrue()
    }

    @Test
    fun `should not navigate when submitting blank token`() {
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged("   "))

        viewModel.onAction(TokenScreenAction.Confirm)

        assertThat(viewModel.uiState.value.navigateToMatches).isFalse()
    }

    @Test
    fun `should reset navigateToMatches after onNavigationConsumed`() = runTest {
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged(TestConstants.TOKEN))
        viewModel.onAction(TokenScreenAction.Confirm)
        assertThat(viewModel.uiState.value.navigateToMatches).isTrue()

        viewModel.onNavigationConsumed()

        assertThat(viewModel.uiState.value.navigateToMatches).isFalse()
    }

    @Test
    fun `should emit error state when save token fails`() = runTest {
        val exception = RuntimeException("Save failed")
        every { mockSessionLocalDataSource.saveToken(any()) } throws exception
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged(TestConstants.TOKEN))

        viewModel.onAction(TokenScreenAction.Confirm)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.error).isEqualTo(exception)
    }

    @Test
    fun `should flip isDarkTheme and persist new value when ToggleTheme action is dispatched`() = runTest {
        every { mockSessionLocalDataSource.isDarkTheme() } returns false
        val viewModel = createViewModel()

        viewModel.onAction(TokenScreenAction.ToggleTheme)
        advanceUntilIdle()

        assertThat(viewModel.uiState.value.isDarkTheme).isTrue()
        verify { mockSessionLocalDataSource.saveDarkTheme(true) }
        verify { mockThemeManager.apply(true) }
    }

    @Test
    fun `should switch language from EN to PT and emit RecreateActivity when ToggleLanguage action is dispatched`() = runTest {
        every { mockSessionLocalDataSource.getLanguage() } returns Language.EN
        val viewModel = createViewModel()

        viewModel.navigationEvents.test {
            viewModel.onAction(TokenScreenAction.ToggleLanguage)
            assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(Language.PT)
            assertThat(awaitItem()).isEqualTo(TokenNavigationEvent.RecreateActivity)
            cancelAndConsumeRemainingEvents()
        }
        verify { mockSessionLocalDataSource.saveLanguage(Language.PT) }
        verify { mockLocaleManager.apply(Language.PT) }
    }

    @Test
    fun `should set showTutorialDialog to true when ShowTutorial action is dispatched`() {
        val viewModel = createViewModel()

        viewModel.onAction(TokenScreenAction.ShowTutorial)

        assertThat(viewModel.uiState.value.showTutorialDialog).isTrue()
    }

    @Test
    fun `should set showTutorialDialog to false when DismissTutorial action is dispatched`() {
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.ShowTutorial)
        assertThat(viewModel.uiState.value.showTutorialDialog).isTrue()

        viewModel.onAction(TokenScreenAction.DismissTutorial)

        assertThat(viewModel.uiState.value.showTutorialDialog).isFalse()
    }

    @Test
    fun `should start demo and navigate to matches when TryDemo action is dispatched`() {
        val viewModel = createViewModel()

        viewModel.onAction(TokenScreenAction.TryDemo)

        verify { mockDemoSessionManager.startDemo() }
        assertThat(viewModel.uiState.value.navigateToMatches).isTrue()
    }

    @Test
    fun `should set token and close tutorial when PasteTokenFromClipboard is dispatched with text`() {
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.ShowTutorial)

        viewModel.onAction(TokenScreenAction.PasteTokenFromClipboard(TestConstants.TOKEN_CLIPBOARD))

        assertThat(viewModel.uiState.value.token).isEqualTo(TestConstants.TOKEN_CLIPBOARD)
        assertThat(viewModel.uiState.value.showTutorialDialog).isFalse()
    }

    @Test
    fun `should keep token unchanged and close tutorial when PasteTokenFromClipboard is dispatched with null`() {
        val viewModel = createViewModel()
        viewModel.onAction(TokenScreenAction.OnTokenChanged(TestConstants.TOKEN_EXISTING))
        viewModel.onAction(TokenScreenAction.ShowTutorial)

        viewModel.onAction(TokenScreenAction.PasteTokenFromClipboard(null))

        assertThat(viewModel.uiState.value.token).isEqualTo(TestConstants.TOKEN_EXISTING)
        assertThat(viewModel.uiState.value.showTutorialDialog).isFalse()
    }

    @Test
    fun `should trim whitespace from clipboard text when PasteTokenFromClipboard is dispatched with spaces`() {
        val viewModel = createViewModel()

        viewModel.onAction(TokenScreenAction.PasteTokenFromClipboard(TestConstants.TOKEN_WITH_SPACES))

        assertThat(viewModel.uiState.value.token).isEqualTo(TestConstants.TOKEN_TRIMMED)
    }
}
