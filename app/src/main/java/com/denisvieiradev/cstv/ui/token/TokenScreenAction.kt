package com.denisvieiradev.cstv.ui.token

sealed interface TokenScreenAction {
    data class OnTokenChanged(val value: String) : TokenScreenAction
    data object Confirm : TokenScreenAction
    data object TryDemo : TokenScreenAction
    data object ToggleTheme : TokenScreenAction
    data object ToggleLanguage : TokenScreenAction
    data object ShowTutorial : TokenScreenAction
    data object DismissTutorial : TokenScreenAction
    data object ShowDemoConfirmation : TokenScreenAction
    data object DismissDemoConfirmation : TokenScreenAction
    data class PasteTokenFromClipboard(val clipboardText: String?) : TokenScreenAction
}
