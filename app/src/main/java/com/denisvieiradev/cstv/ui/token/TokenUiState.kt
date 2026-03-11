package com.denisvieiradev.cstv.ui.token

import com.denisvieiradev.cstv.domain.Language

data class TokenUiState(
    val token: String = "",
    val isLoading: Boolean = false,
    val error: Throwable? = null,
    val navigateToMatches: Boolean = false,
    val isDarkTheme: Boolean = true,
    val currentLanguage: String = Language.EN,
    val showTutorialDialog: Boolean = false,
    val showDemoConfirmationDialog: Boolean = false
) {
    val isConfirmEnabled: Boolean get() = token.isNotBlank()
}
