package com.denisvieiradev.cstv.ui.matches

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import com.denisvieiradev.cstv.ui.core.LocaleManager
import com.denisvieiradev.cstv.ui.core.ThemeManager
import com.denisvieiradev.cstv.ui.matches.model.MatchesNavigationEvent
import com.denisvieiradev.cstv.ui.matches.model.MatchesScreenAction
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.denisvieiradev.cstv.utils.TestConstants
import com.denisvieiradev.cstv.utils.fakeMatch
import com.denisvieiradev.network.data.remote.utils.AuthorizationException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class MatchesViewModelTest {

    @RunWith(Enclosed::class)
    class GivenDemoSessionIsActive {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenViewModelIsCreated : MatchesViewModelTestBase()  {

            @Test
            fun `then uiState emits loading`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    assertThat(awaitItem().isLoading).isTrue()
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then showDemoExpiredDialog is false`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    assertThat(awaitItem().showDemoExpiredDialog).isFalse()
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then isDarkTheme is read from saved preference`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                every { mockSession.isDarkTheme() } returns false
                val viewModel = createViewModel()

                assertThat(viewModel.uiState.value.isDarkTheme).isFalse()
            }

            @Test
            fun `then currentLanguage is read from saved preference`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                every { mockSession.getLanguage() } returns Language.PT
                val viewModel = createViewModel()

                assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(Language.PT)
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenLoadMatchesSucceeds : MatchesViewModelTestBase() {

            @Test
            fun `then uiState contains the matches list`() = runTest {
                val matches = listOf(fakeMatch())
                coEvery { mockUseCase() } returns matches
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    val successState = awaitItem()
                    assertThat(successState.matches).isEqualTo(matches)
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then isLoading is false`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    assertThat(awaitItem().isLoading).isFalse()
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then isAuthError is false`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    assertThat(awaitItem().isAuthError).isFalse()
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenLoadMatchesFails : MatchesViewModelTestBase() {

            @Test
            fun `then uiState contains the exception`() = runTest {
                val exception = RuntimeException("Network error")
                coEvery { mockUseCase() } throws exception
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    val errorState = awaitItem()
                    assertThat(errorState.error).isInstanceOf(RuntimeException::class.java)
                    assertThat(errorState.error?.message).isEqualTo(exception.message)
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then isLoading is false`() = runTest {
                coEvery { mockUseCase() } throws RuntimeException("Network error")
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    assertThat(awaitItem().isLoading).isFalse()
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then isAuthError is false`() = runTest {
                coEvery { mockUseCase() } throws RuntimeException("Network error")
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    assertThat(awaitItem().isAuthError).isFalse()
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenAuthorizationFails : MatchesViewModelTestBase() {

            @Test
            fun `then isAuthError is true`() = runTest {
                coEvery { mockUseCase() } throws AuthorizationException(TestConstants.HTTP_UNAUTHORIZED)
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    assertThat(awaitItem().isAuthError).isTrue()
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then error is null`() = runTest {
                coEvery { mockUseCase() } throws AuthorizationException(TestConstants.HTTP_UNAUTHORIZED)
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    assertThat(awaitItem().error).isNull()
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then isLoading is false`() = runTest {
                coEvery { mockUseCase() } throws AuthorizationException(TestConstants.HTTP_UNAUTHORIZED)
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    assertThat(awaitItem().isLoading).isFalse()
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenRetryIsDispatched : MatchesViewModelTestBase() {

            @Test
            fun `then matches are reloaded`() = runTest {
                val initialMatches = listOf(fakeMatch(id = 1))
                val retryMatches = listOf(fakeMatch(id = 2))
                coEvery { mockUseCase() } returnsMany listOf(initialMatches, retryMatches)
                val viewModel = createViewModel()

                viewModel.uiState.test {
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
    }

    @RunWith(Enclosed::class)
    class GivenDemoSessionIsExpired {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenOpenMatchDetailIsDispatched : MatchesViewModelTestBase() {

            init {
                every { mockDemo.tryConsume() } returns false
            }

            @Test
            fun `then showDemoExpiredDialog is true`() = runTest {
                val match = fakeMatch(id = 1)
                coEvery { mockUseCase() } returns listOf(match)
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.OpenMatchDetail(match))

                assertThat(viewModel.uiState.value.showDemoExpiredDialog).isTrue()
            }

            @Test
            fun `then OpenMatchDetail event is NOT emitted`() = runTest {
                val match = fakeMatch(id = 1)
                coEvery { mockUseCase() } returns listOf(match)
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.OpenMatchDetail(match))
                    expectNoEvents()
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenDismissDemoExpiredIsDispatched : MatchesViewModelTestBase() {

            @Test
            fun `then demo session is reset`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.DismissDemoExpired)
                    awaitItem() // NavigateToTokenScreen
                    cancelAndConsumeRemainingEvents()
                }
                verify { mockDemo.reset() }
            }

            @Test
            fun `then NavigateToTokenScreen is emitted`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.DismissDemoExpired)
                    assertThat(awaitItem()).isEqualTo(MatchesNavigationEvent.NavigateToTokenScreen)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenUserIsLoggedIn {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenLogoutIsDispatched : MatchesViewModelTestBase() {

            @Test
            fun `then showLogoutDialog is true`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.PressLogout)

                assertThat(viewModel.uiState.value.showLogoutDialog).isTrue()
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenConfirmLogoutIsDispatched : MatchesViewModelTestBase() {

            @Test
            fun `then NavigateToTokenScreen is emitted`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.ConfirmLogout)
                    assertThat(awaitItem()).isEqualTo(MatchesNavigationEvent.NavigateToTokenScreen)
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then session is cleared`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.ConfirmLogout)
                    awaitItem()
                    cancelAndConsumeRemainingEvents()
                }
                verify { mockSession.clearSession() }
            }

            @Test
            fun `then showLogoutDialog is false`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()
                viewModel.onAction(MatchesScreenAction.PressLogout)
                assertThat(viewModel.uiState.value.showLogoutDialog).isTrue()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.ConfirmLogout)
                    awaitItem()
                    cancelAndConsumeRemainingEvents()
                }
                advanceUntilIdle()

                assertThat(viewModel.uiState.value.showLogoutDialog).isFalse()
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenDismissLogoutIsDispatched : MatchesViewModelTestBase() {

            @Test
            fun `then showLogoutDialog is false`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()
                viewModel.onAction(MatchesScreenAction.PressLogout)
                assertThat(viewModel.uiState.value.showLogoutDialog).isTrue()

                viewModel.onAction(MatchesScreenAction.DismissLogout)

                assertThat(viewModel.uiState.value.showLogoutDialog).isFalse()
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenConfigureTokenIsDispatched : MatchesViewModelTestBase() {

            @Test
            fun `then session is cleared and NavigateToTokenScreen is emitted`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.ConfigureToken)
                    assertThat(awaitItem()).isEqualTo(MatchesNavigationEvent.NavigateToTokenScreen)
                    cancelAndConsumeRemainingEvents()
                }
                verify { mockSession.clearSession() }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenThemeIsDark {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenToggleThemeIsDispatched : MatchesViewModelTestBase() {

            init {
                every { mockSession.isDarkTheme() } returns true
            }

            @Test
            fun `then isDarkTheme flips to false`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleTheme)
                advanceUntilIdle()

                assertThat(viewModel.uiState.value.isDarkTheme).isFalse()
            }

            @Test
            fun `then new value is persisted`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleTheme)
                advanceUntilIdle()

                verify { mockSession.saveDarkTheme(false) }
            }

            @Test
            fun `then ThemeManager apply is called with false`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleTheme)
                advanceUntilIdle()

                verify { mockThemeManager.apply(false) }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenLanguageIsEN {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenToggleLanguageIsDispatched : MatchesViewModelTestBase() {

            init {
                every { mockSession.getLanguage() } returns Language.EN
            }

            @Test
            fun `then currentLanguage switches to PT`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleLanguage)
                advanceUntilIdle()

                assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(Language.PT)
            }

            @Test
            fun `then new language is persisted`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleLanguage)
                advanceUntilIdle()

                verify { mockSession.saveLanguage(Language.PT) }
            }

            @Test
            fun `then LocaleManager apply is called with PT`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleLanguage)
                advanceUntilIdle()

                verify { mockLocaleManager.apply(Language.PT) }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenLanguageIsPT {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenToggleLanguageIsDispatched : MatchesViewModelTestBase() {

            init {
                every { mockSession.getLanguage() } returns Language.PT
            }

            @Test
            fun `then currentLanguage switches to EN`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleLanguage)
                advanceUntilIdle()

                assertThat(viewModel.uiState.value.currentLanguage).isEqualTo(Language.EN)
            }

            @Test
            fun `then new language is persisted`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleLanguage)
                advanceUntilIdle()

                verify { mockSession.saveLanguage(Language.EN) }
            }

            @Test
            fun `then LocaleManager apply is called with EN`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.onAction(MatchesScreenAction.ToggleLanguage)
                advanceUntilIdle()

                verify { mockLocaleManager.apply(Language.EN) }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenMatchExists {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenOpenMatchDetailIsDispatched : MatchesViewModelTestBase() {

            @Test
            fun `then OpenMatchDetail navigation event is emitted with the match`() = runTest {
                val match = fakeMatch(id = 42)
                coEvery { mockUseCase() } returns listOf(match)
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.OpenMatchDetail(match))
                    val event = awaitItem()
                    assertThat(event).isInstanceOf(MatchesNavigationEvent.OpenMatchDetail::class.java)
                    assertThat((event as MatchesNavigationEvent.OpenMatchDetail).match).isEqualTo(match)
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then tryConsume is called`() = runTest {
                val match = fakeMatch(id = 42)
                coEvery { mockUseCase() } returns listOf(match)
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.OpenMatchDetail(match))
                    awaitItem()
                    cancelAndConsumeRemainingEvents()
                }
                verify { mockDemo.tryConsume() }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenLocaleChangeRequiresRecreation {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenToggleLanguageIsDispatched : MatchesViewModelTestBase() {

            init {
                every { mockLocaleManager.apply(any()) } returns true
            }

            @Test
            fun `then RecreateActivity navigation event is emitted`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.ToggleLanguage)
                    advanceUntilIdle()
                    assertThat(awaitItem()).isEqualTo(MatchesNavigationEvent.RecreateActivity)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenLocaleChangeDoesNotRequireRecreation {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenToggleLanguageIsDispatched : MatchesViewModelTestBase() {

            init {
                every { mockLocaleManager.apply(any()) } returns false
            }

            @Test
            fun `then RecreateActivity navigation event is NOT emitted`() = runTest {
                coEvery { mockUseCase() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(MatchesScreenAction.ToggleLanguage)
                    advanceUntilIdle()
                    expectNoEvents()
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }

}

abstract class MatchesViewModelTestBase {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val mockUseCase: GetCsMatchesUseCase = mockk()
    val mockSession: SessionLocalDataSource = mockk(relaxed = true)
    val mockDemo: DemoSessionManager = mockk(relaxed = true) {
        every { tryConsume() } returns true
    }
    val mockThemeManager: ThemeManager = mockk(relaxed = true)
    val mockLocaleManager: LocaleManager = mockk(relaxed = true)

    fun createViewModel() = MatchesViewModel(
        getCsMatchesUseCase = mockUseCase,
        sessionLocalDataSource = mockSession,
        demoSessionManager = mockDemo,
        ioDispatcher = mainDispatcherRule.testDispatcher,
        themeManager = mockThemeManager,
        localeManager = mockLocaleManager
    )
}
