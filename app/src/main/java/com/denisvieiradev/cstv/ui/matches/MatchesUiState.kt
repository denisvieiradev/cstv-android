package com.denisvieiradev.cstv.ui.matches

import com.denisvieiradev.cstv.domain.model.Match

data class MatchesUiState(
    val isLoading: Boolean = false,
    val matches: List<Match> = emptyList(),
    val error: Throwable? = null
) {
    val hasError: Boolean get() = error != null
    val isEmpty: Boolean get() = !isLoading && matches.isEmpty() && !hasError
}
