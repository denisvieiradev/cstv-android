package com.denisvieiradev.cstv.ui.core

import androidx.appcompat.app.AppCompatDelegate
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppCompatThemeManagerTest {

    private val themeManager = AppCompatThemeManager()

    @Before
    fun setUp() {
        mockkStatic(AppCompatDelegate::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(AppCompatDelegate::class)
    }

    @Test
    fun `should set MODE_NIGHT_YES when isDark is true`() {
        themeManager.apply(true)

        verify { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES) }
    }

    @Test
    fun `should set MODE_NIGHT_NO when isDark is false`() {
        themeManager.apply(false)

        verify { AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) }
    }
}
