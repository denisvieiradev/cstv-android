package com.denisvieiradev.cstv.ui.token

data class TokenUiState(
    val token: String = "",
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val navigateToMatches: Boolean = false
) {
    val isConfirmEnabled: Boolean get() = token.isNotBlank()
}
