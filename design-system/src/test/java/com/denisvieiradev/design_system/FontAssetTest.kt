package com.denisvieiradev.design_system

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.io.File

class FontAssetTest {

    private val fontDir = File("src/main/res/font")
    private val fontFiles = listOf("roboto_regular.ttf", "roboto_medium.ttf", "roboto_bold.ttf")
    private val htmlSignature = byteArrayOf('<'.code.toByte(), '!'.code.toByte(), 'D'.code.toByte(), 'O'.code.toByte())

    @Test
    fun `font files should exist and be larger than 50 KB`() {
        fontFiles.forEach { name ->
            val file = File(fontDir, name)
            assertThat(file.exists()).isTrue()
            assertThat(file.length()).isGreaterThan(50 * 1024L)
        }
    }

    @Test
    fun `font files should not start with HTML DOCTYPE signature`() {
        fontFiles.forEach { name ->
            val bytes = File(fontDir, name).readBytes()
            val firstFour = bytes.take(4).toByteArray()
            assertThat(firstFour).isNotEqualTo(htmlSignature)
        }
    }
}
