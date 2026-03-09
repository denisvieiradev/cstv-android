package com.denisvieiradev.cstv.ui.token

sealed interface TokenScreenAction {
    data class OnTokenChanged(val value: String) : TokenScreenAction
    data object Confirm : TokenScreenAction
}
