package com.denisvieiradev.cstv.data.session

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DemoSessionManagerTest {

    @Test
    fun `should set isActive to true and reset remaining views when startDemo is called`() {
        val manager = DemoSessionManager()

        manager.startDemo()

        assertThat(manager.isActive).isTrue()
    }

    @Test
    fun `should return true six times and false on seventh call when tryConsume is called`() {
        val manager = DemoSessionManager()
        manager.startDemo()

        assertThat(manager.tryConsume()).isTrue()
        assertThat(manager.tryConsume()).isTrue()
        assertThat(manager.tryConsume()).isTrue()
        assertThat(manager.tryConsume()).isTrue()
        assertThat(manager.tryConsume()).isTrue()
        assertThat(manager.tryConsume()).isTrue()
        assertThat(manager.tryConsume()).isFalse()
    }

    @Test
    fun `should set isActive to false and reset remaining views when reset is called`() {
        val manager = DemoSessionManager()
        manager.startDemo()
        manager.tryConsume()

        manager.reset()

        assertThat(manager.isActive).isFalse()
    }

    @Test
    fun `should return true when tryConsume is called and demo is not active`() {
        val manager = DemoSessionManager()

        val result = manager.tryConsume()

        assertThat(result).isTrue()
    }

    @Test
    fun `should keep returning false after limit is exhausted`() {
        val manager = DemoSessionManager()
        manager.startDemo()
        repeat(DemoSessionManager.DEMO_DETAIL_LIMIT) { manager.tryConsume() }

        assertThat(manager.tryConsume()).isFalse()
        assertThat(manager.tryConsume()).isFalse()
    }
}
