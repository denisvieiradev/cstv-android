package com.denisvieiradev.cstv.data.session

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DemoSessionManagerTest {

    @Test
    fun `should set isActive to true and reset remaining requests when startDemo is called`() {
        // Arrange
        val manager = DemoSessionManager()

        // Act
        manager.startDemo()

        // Assert
        assertThat(manager.isActive).isTrue()
    }

    @Test
    fun `should return true three times and false on fourth call when tryConsume is called with 2`() {
        // Arrange
        val manager = DemoSessionManager()
        manager.startDemo()

        // Act / Assert
        assertThat(manager.tryConsume(2)).isTrue()
        assertThat(manager.tryConsume(2)).isTrue()
        assertThat(manager.tryConsume(2)).isTrue()
        assertThat(manager.tryConsume(2)).isFalse()
    }

    @Test
    fun `should set isActive to false and reset remaining requests when reset is called`() {
        // Arrange
        val manager = DemoSessionManager()
        manager.startDemo()
        manager.tryConsume(2)

        // Act
        manager.reset()

        // Assert
        assertThat(manager.isActive).isFalse()
    }

    @Test
    fun `should return true when tryConsume is called and demo is not active`() {
        // Arrange
        val manager = DemoSessionManager()

        // Act
        val result = manager.tryConsume(2)

        // Assert
        assertThat(result).isTrue()
    }
}
