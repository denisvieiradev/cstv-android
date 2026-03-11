package com.denisvieiradev.cstv.integration

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.data.datasources.remote.matches.MatchRemoteDataSource
import com.denisvieiradev.cstv.data.repository.MatchRepositoryImpl
import com.denisvieiradev.cstv.data.session.DemoSessionManager
import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.domain.usecase.GetCsMatchesUseCase
import com.denisvieiradev.cstv.ui.matches.MatchesViewModel
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.denisvieiradev.cstv.utils.TestConstants
import com.denisvieiradev.cstv.utils.fakeMatch
import com.denisvieiradev.network.data.remote.utils.AuthorizationException
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class MatchesFlowIntegrationTest {

    @RunWith(Enclosed::class)
    class GivenRealLayersConnected {

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenRemoteDataSourceReturnsData {

            @get:Rule
            val mainDispatcherRule = MainDispatcherRule()

            private val mockDataSource: MatchRemoteDataSource = mockk()
            private val repository = MatchRepositoryImpl(mockDataSource)
            private val useCase = GetCsMatchesUseCase(repository)
            private val mockSession: SessionLocalDataSource = mockk(relaxed = true)
            private val mockDemo: DemoSessionManager = mockk(relaxed = true) {
                every { tryConsume(any()) } returns true
            }

            private val mockThemeManager: com.denisvieiradev.cstv.ui.core.ThemeManager = mockk(relaxed = true)
            private val mockLocaleManager: com.denisvieiradev.cstv.ui.core.LocaleManager = mockk(relaxed = true)

            private fun createViewModel() = MatchesViewModel(
                getCsMatchesUseCase = useCase,
                sessionLocalDataSource = mockSession,
                demoSessionManager = mockDemo,
                ioDispatcher = mainDispatcherRule.testDispatcher,
                themeManager = mockThemeManager,
                localeManager = mockLocaleManager
            )

            @Test
            fun `then ViewModel emits loading then success with combined list`() = runTest {
                val runningMatches = listOf(fakeMatch(id = 1, status = MatchStatus.RUNNING))
                val upcomingMatches = listOf(fakeMatch(id = 2, status = MatchStatus.NOT_STARTED))
                coEvery { mockDataSource.getRunningMatches() } returns runningMatches
                coEvery { mockDataSource.getUpcomingMatches() } returns upcomingMatches
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    assertThat(awaitItem().isLoading).isTrue()
                    val successState = awaitItem()
                    assertThat(successState.isLoading).isFalse()
                    assertThat(successState.matches).hasSize(2)
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then running matches appear first in the list`() = runTest {
                val running = fakeMatch(id = 1, status = MatchStatus.RUNNING)
                val upcoming = fakeMatch(id = 2, status = MatchStatus.NOT_STARTED)
                coEvery { mockDataSource.getRunningMatches() } returns listOf(running)
                coEvery { mockDataSource.getUpcomingMatches() } returns listOf(upcoming)
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    val successState = awaitItem()
                    assertThat(successState.matches[0]).isEqualTo(running)
                    assertThat(successState.matches[1]).isEqualTo(upcoming)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenRemoteDataSourceThrowsAuthorizationException {

            @get:Rule
            val mainDispatcherRule = MainDispatcherRule()

            private val mockDataSource: MatchRemoteDataSource = mockk()
            private val repository = MatchRepositoryImpl(mockDataSource)
            private val useCase = GetCsMatchesUseCase(repository)
            private val mockSession: SessionLocalDataSource = mockk(relaxed = true)
            private val mockDemo: DemoSessionManager = mockk(relaxed = true) {
                every { tryConsume(any()) } returns true
            }

            private val mockThemeManager: com.denisvieiradev.cstv.ui.core.ThemeManager = mockk(relaxed = true)
            private val mockLocaleManager: com.denisvieiradev.cstv.ui.core.LocaleManager = mockk(relaxed = true)

            private fun createViewModel() = MatchesViewModel(
                getCsMatchesUseCase = useCase,
                sessionLocalDataSource = mockSession,
                demoSessionManager = mockDemo,
                ioDispatcher = mainDispatcherRule.testDispatcher,
                themeManager = mockThemeManager,
                localeManager = mockLocaleManager
            )

            @Test
            fun `then ViewModel uiState sets isAuthError to true`() = runTest {
                coEvery { mockDataSource.getRunningMatches() } throws AuthorizationException(401)
                coEvery { mockDataSource.getUpcomingMatches() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    val errorState = awaitItem()
                    assertThat(errorState.isAuthError).isTrue()
                    assertThat(errorState.error).isNull()
                    cancelAndConsumeRemainingEvents()
                }
            }

            @Test
            fun `then ViewModel uiState has isLoading false after auth error`() = runTest {
                coEvery { mockDataSource.getRunningMatches() } throws AuthorizationException(401)
                coEvery { mockDataSource.getUpcomingMatches() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    val errorState = awaitItem()
                    assertThat(errorState.isLoading).isFalse()
                    assertThat(errorState.isAuthError).isTrue()
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenRemoteDataSourceThrowsGenericException {

            @get:Rule
            val mainDispatcherRule = MainDispatcherRule()

            private val mockDataSource: MatchRemoteDataSource = mockk()
            private val repository = MatchRepositoryImpl(mockDataSource)
            private val useCase = GetCsMatchesUseCase(repository)
            private val mockSession: SessionLocalDataSource = mockk(relaxed = true)
            private val mockDemo: DemoSessionManager = mockk(relaxed = true) {
                every { tryConsume(any()) } returns true
            }

            private val mockThemeManager: com.denisvieiradev.cstv.ui.core.ThemeManager = mockk(relaxed = true)
            private val mockLocaleManager: com.denisvieiradev.cstv.ui.core.LocaleManager = mockk(relaxed = true)

            private fun createViewModel() = MatchesViewModel(
                getCsMatchesUseCase = useCase,
                sessionLocalDataSource = mockSession,
                demoSessionManager = mockDemo,
                ioDispatcher = mainDispatcherRule.testDispatcher,
                themeManager = mockThemeManager,
                localeManager = mockLocaleManager
            )

            @Test
            fun `then ViewModel uiState contains the exception`() = runTest {
                val exception = RuntimeException(TestConstants.ERROR_NETWORK)
                coEvery { mockDataSource.getRunningMatches() } throws exception
                coEvery { mockDataSource.getUpcomingMatches() } returns emptyList()
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
            fun `then ViewModel uiState does not set isAuthError`() = runTest {
                val exception = RuntimeException(TestConstants.ERROR_NETWORK)
                coEvery { mockDataSource.getRunningMatches() } throws exception
                coEvery { mockDataSource.getUpcomingMatches() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    val errorState = awaitItem()
                    assertThat(errorState.isAuthError).isFalse()
                    cancelAndConsumeRemainingEvents()
                }
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        class WhenBothEndpointsReturnEmpty {

            @get:Rule
            val mainDispatcherRule = MainDispatcherRule()

            private val mockDataSource: MatchRemoteDataSource = mockk()
            private val repository = MatchRepositoryImpl(mockDataSource)
            private val useCase = GetCsMatchesUseCase(repository)
            private val mockSession: SessionLocalDataSource = mockk(relaxed = true)
            private val mockDemo: DemoSessionManager = mockk(relaxed = true) {
                every { tryConsume(any()) } returns true
            }

            private val mockThemeManager: com.denisvieiradev.cstv.ui.core.ThemeManager = mockk(relaxed = true)
            private val mockLocaleManager: com.denisvieiradev.cstv.ui.core.LocaleManager = mockk(relaxed = true)

            private fun createViewModel() = MatchesViewModel(
                getCsMatchesUseCase = useCase,
                sessionLocalDataSource = mockSession,
                demoSessionManager = mockDemo,
                ioDispatcher = mainDispatcherRule.testDispatcher,
                themeManager = mockThemeManager,
                localeManager = mockLocaleManager
            )

            @Test
            fun `then ViewModel uiState has empty matches list`() = runTest {
                coEvery { mockDataSource.getRunningMatches() } returns emptyList()
                coEvery { mockDataSource.getUpcomingMatches() } returns emptyList()
                val viewModel = createViewModel()

                viewModel.uiState.test {
                    awaitItem() // loading
                    val successState = awaitItem()
                    assertThat(successState.matches).isEmpty()
                    assertThat(successState.isLoading).isFalse()
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }
}
