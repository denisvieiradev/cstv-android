package com.denisvieiradev.cstv.core

import com.denisvieiradev.cstv.BuildConfig
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import timber.log.Timber

class AppLoggingTest {

    @Before
    fun setUp() {
        Timber.uprootAll()
    }

    @After
    fun tearDown() {
        Timber.uprootAll()
    }

    @Test
    fun `timber tree is planted only in debug builds`() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        val expectedSize = if (BuildConfig.DEBUG) 1 else 0
        assertThat(Timber.forest()).hasSize(expectedSize)
    }

    @Test
    fun `no timber tree is planted by default`() {
        assertThat(Timber.forest()).isEmpty()
    }
}
