package com.denisvieiradev.cstv.ui.matchdetail

import com.denisvieiradev.cstv.domain.model.Match

class SelectedMatchHolder {
    private var match: Match? = null
    fun set(match: Match) { this.match = match }
    fun get(): Match? = match
    fun clear() { match = null }
}
