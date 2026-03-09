package com.denisvieiradev.cstv.ui.token

import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class TokenViewModelTest {

    private val mockSessionRepository: SessionRepository = mockk(relaxed = true)

    @Test
    fun `should disable confirm button when token is blank`() {
        // Arrange
        val viewModel = TokenViewModel(mockSessionRepository)

        // Act / Assert
        assertThat(viewModel.uiState.value.isConfirmEnabled).isFalse()
    }

    @Test
    fun `should enable confirm button when token is not blank`() {
        // Arrange
        val viewModel = TokenViewModel(mockSessionRepository)

        // Act
        viewModel.onAction(TokenScreenAction.OnTokenChanged("my-token"))

        // Assert
        assertThat(viewModel.uiState.value.isConfirmEnabled).isTrue()
    }

    @Test
    fun `should save token when confirm action is dispatched`() {
        // Arrange
        val viewModel = TokenViewModel(mockSessionRepository)
        viewModel.onAction(TokenScreenAction.OnTokenChanged("my-token"))

        // Act
        viewModel.onAction(TokenScreenAction.Confirm)

        // Assert
        verify { mockSessionRepository.saveToken("my-token") }
        assertThat(viewModel.uiState.value.navigateToMatches).isTrue()
    }

    @Test
    fun `should not navigate when submitting blank token`() {
        // Arrange
        val viewModel = TokenViewModel(mockSessionRepository)
        viewModel.onAction(TokenScreenAction.OnTokenChanged("   "))

        // Act
        viewModel.onAction(TokenScreenAction.Confirm)

        // Assert
        assertThat(viewModel.uiState.value.navigateToMatches).isFalse()
    }

    @Test
    fun `should reset navigateToMatches after onNavigationConsumed`() {
        // Arrange
        val viewModel = TokenViewModel(mockSessionRepository)
        viewModel.onAction(TokenScreenAction.OnTokenChanged("my-token"))
        viewModel.onAction(TokenScreenAction.Confirm)
        assertThat(viewModel.uiState.value.navigateToMatches).isTrue()

        // Act
        viewModel.onNavigationConsumed()

        // Assert
        assertThat(viewModel.uiState.value.navigateToMatches).isFalse()
    }

    @Test
    fun `should emit error state when save token fails`() {
        // Arrange
        val exception = RuntimeException("Save failed")
        every { mockSessionRepository.saveToken(any()) } throws exception
        val viewModel = TokenViewModel(mockSessionRepository)
        viewModel.onAction(TokenScreenAction.OnTokenChanged("my-token"))

        // Act
        viewModel.onAction(TokenScreenAction.Confirm)

        // Assert
        assertThat(viewModel.uiState.value.error).isEqualTo(exception)
    }
}
