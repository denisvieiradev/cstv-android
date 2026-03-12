package com.denisvieiradev.cstv.ui.core

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppCompatLocaleManagerTest {

    private val localeManager = AppCompatLocaleManager()

    @Before
    fun setUp() {
        mockkStatic(AppCompatDelegate::class)
        mockkStatic(LocaleListCompat::class)
    }

    @After
    fun tearDown() {
        unmockkStatic(AppCompatDelegate::class)
        unmockkStatic(LocaleListCompat::class)
    }

    @Test
    fun `should apply English locale`() {
        val fakeLocaleList = LocaleListCompat.getEmptyLocaleList()
        every { LocaleListCompat.forLanguageTags("en") } returns fakeLocaleList

        localeManager.apply("en")

        verify { AppCompatDelegate.setApplicationLocales(fakeLocaleList) }
    }

    @Test
    fun `should apply Portuguese locale`() {
        val fakeLocaleList = LocaleListCompat.getEmptyLocaleList()
        every { LocaleListCompat.forLanguageTags("pt-BR") } returns fakeLocaleList

        localeManager.apply("pt-BR")

        verify { AppCompatDelegate.setApplicationLocales(fakeLocaleList) }
    }
}
