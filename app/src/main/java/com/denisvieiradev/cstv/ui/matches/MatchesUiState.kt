package com.denisvieiradev.cstv.ui.matches

import com.denisvieiradev.cstv.data.datasources.local.SessionRepositoryImpl
import com.denisvieiradev.cstv.domain.model.Match

data class MatchesUiState(
    val isLoading: Boolean = false,
    val matches: List<Match> = emptyList(),
    val error: Throwable? = null,
    val isAuthError: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val isDarkTheme: Boolean = true,
    val currentLanguage: String = SessionRepositoryImpl.LANG_EN
) {
    val hasError: Boolean get() = error != null || isAuthError
    val isEmpty: Boolean get() = !isLoading && matches.isEmpty() && !hasError
}
