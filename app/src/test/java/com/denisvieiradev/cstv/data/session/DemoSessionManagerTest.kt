package com.denisvieiradev.cstv.data.session

import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.utils.TestConstants
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class DemoSessionManagerTest {

    private val mockSession: SessionLocalDataSource = mockk(relaxed = true) {
        every { isDemoUsed() } returns false
        every { getToken() } returns null
    }
    private val demoToken = TestConstants.DEMO_TOKEN

    private fun createManager() = DemoSessionManager(mockSession, demoToken)

    @Test
    fun `should set isActive to true and reset remaining views when startDemo is called`() {
        val manager = createManager()

        manager.startDemo()

        assertThat(manager.isActive).isTrue()
    }

    @Test
    fun `should return true six times and false on seventh call when tryConsume is called`() {
        val manager = createManager()
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
        val manager = createManager()
        manager.startDemo()
        manager.tryConsume()

        manager.reset()

        assertThat(manager.isActive).isFalse()
    }

    @Test
    fun `should return true when tryConsume is called and demo is not active`() {
        val manager = createManager()

        val result = manager.tryConsume()

        assertThat(result).isTrue()
    }

    @Test
    fun `should keep returning false after limit is exhausted`() {
        val manager = createManager()
        manager.startDemo()
        repeat(DemoSessionManager.DEMO_DETAIL_LIMIT) { manager.tryConsume() }

        assertThat(manager.tryConsume()).isFalse()
        assertThat(manager.tryConsume()).isFalse()
    }

    @Test
    fun `startDemo saves demo used flag`() {
        val manager = createManager()

        manager.startDemo()

        verify { mockSession.saveDemoUsed() }
    }

    @Test
    fun `isDemoAlreadyUsed returns true after startDemo`() {
        every { mockSession.isDemoUsed() } returns true
        val manager = createManager()

        assertThat(manager.isDemoAlreadyUsed()).isTrue()
    }

    @Test
    fun `on init, restores expired state when isDemoUsed and token matches`() {
        every { mockSession.isDemoUsed() } returns true
        every { mockSession.getToken() } returns demoToken
        val manager = createManager()

        assertThat(manager.isActive).isTrue()
        assertThat(manager.tryConsume()).isFalse()
    }

    @Test
    fun `on init, does not restore when isDemoUsed but token differs`() {
        every { mockSession.isDemoUsed() } returns true
        every { mockSession.getToken() } returns TestConstants.TOKEN
        val manager = createManager()

        assertThat(manager.isActive).isFalse()
        assertThat(manager.tryConsume()).isTrue()
    }
}
