package com.denisvieiradev.cstv.ui.splashscreen

import app.cash.turbine.test
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.ui.splashscreen.model.SplashScreenAction
import com.denisvieiradev.cstv.ui.splashscreen.model.SplashScreenNavigationEvent
import com.denisvieiradev.cstv.utils.MainDispatcherRule
import com.denisvieiradev.cstv.utils.TestConstants
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith

@RunWith(Enclosed::class)
class SplashScreenViewModelTest {

    @RunWith(Enclosed::class)
    class GivenTokenExists {

        class WhenCheckSessionIsDispatched : SplashScreenViewModelTestBase() {

            @Test
            fun `then NavigateToMatches is emitted`() = runTest {
                every { mockSessionLocalDataSource.getToken() } returns TestConstants.TOKEN
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(SplashScreenAction.CheckSession)
                    assertThat(awaitItem()).isEqualTo(SplashScreenNavigationEvent.NavigateToMatches)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }

    @RunWith(Enclosed::class)
    class GivenNoToken {

        class WhenCheckSessionIsDispatched : SplashScreenViewModelTestBase() {

            @Test
            fun `then NavigateToToken is emitted`() = runTest {
                every { mockSessionLocalDataSource.getToken() } returns null
                val viewModel = createViewModel()

                viewModel.navigationEvents.test {
                    viewModel.onAction(SplashScreenAction.CheckSession)
                    assertThat(awaitItem()).isEqualTo(SplashScreenNavigationEvent.NavigateToToken)
                    cancelAndConsumeRemainingEvents()
                }
            }
        }
    }
}

abstract class SplashScreenViewModelTestBase {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val mockSessionLocalDataSource: SessionLocalDataSource = mockk(relaxed = true)

    fun createViewModel() = SplashScreenViewModel(
        sessionLocalDataSource = mockSessionLocalDataSource,
        ioDispatcher = mainDispatcherRule.testDispatcher
    )
}
