package com.denisvieiradev.cstv.ui.matches

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

interface LocaleManager {
    fun apply(language: String)
}

class AppCompatLocaleManager : LocaleManager {
    override fun apply(language: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }
}
