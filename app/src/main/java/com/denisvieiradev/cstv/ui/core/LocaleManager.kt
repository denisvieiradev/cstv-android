package com.denisvieiradev.cstv.ui.core

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

interface LocaleManager {
    fun apply(language: String): Boolean
}

class AppCompatLocaleManager : LocaleManager {
    override fun apply(language: String): Boolean {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
    }
}
