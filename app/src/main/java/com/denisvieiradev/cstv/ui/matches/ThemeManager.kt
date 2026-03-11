package com.denisvieiradev.cstv.ui.matches

import androidx.appcompat.app.AppCompatDelegate

interface ThemeManager {
    fun apply(isDark: Boolean)
}

class AppCompatThemeManager : ThemeManager {
    override fun apply(isDark: Boolean) {
        val mode = if (isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}
