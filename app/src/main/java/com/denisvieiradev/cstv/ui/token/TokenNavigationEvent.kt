package com.denisvieiradev.cstv.ui.token

sealed interface TokenNavigationEvent {
    data object RecreateActivity : TokenNavigationEvent
}
