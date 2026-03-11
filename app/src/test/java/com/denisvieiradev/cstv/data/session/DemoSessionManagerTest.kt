package com.denisvieiradev.cstv.data.session

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DemoSessionManagerTest {

    @Test
    fun `should set isActive to true and reset remaining requests when startDemo is called`() {
        val manager = DemoSessionManager()

        manager.startDemo()

        assertThat(manager.isActive).isTrue()
    }

    @Test
    fun `should return true three times and false on fourth call when tryConsume is called with 2`() {
        val manager = DemoSessionManager()
        manager.startDemo()

        assertThat(manager.tryConsume(2)).isTrue()
        assertThat(manager.tryConsume(2)).isTrue()
        assertThat(manager.tryConsume(2)).isTrue()
        assertThat(manager.tryConsume(2)).isFalse()
    }

    @Test
    fun `should set isActive to false and reset remaining requests when reset is called`() {
        val manager = DemoSessionManager()
        manager.startDemo()
        manager.tryConsume(2)

        manager.reset()

        assertThat(manager.isActive).isFalse()
    }

    @Test
    fun `should return true when tryConsume is called and demo is not active`() {
        val manager = DemoSessionManager()

        val result = manager.tryConsume(2)

        assertThat(result).isTrue()
    }

    @Test
    fun `should keep returning false after limit is exhausted`() {
        val manager = DemoSessionManager()
        manager.startDemo()
        repeat(DemoSessionManager.DEMO_PAGE_LIMIT) { manager.tryConsume(DemoSessionManager.REQUESTS_PER_PAGE_LOAD) }

        assertThat(manager.tryConsume(DemoSessionManager.REQUESTS_PER_PAGE_LOAD)).isFalse()
        assertThat(manager.tryConsume(DemoSessionManager.REQUESTS_PER_PAGE_LOAD)).isFalse()
    }
}
