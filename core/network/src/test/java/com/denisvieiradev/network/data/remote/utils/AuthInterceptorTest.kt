package com.denisvieiradev.network.data.remote.utils

import io.mockk.every
import io.mockk.mockk
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertThrows
import org.junit.Assert.assertEquals
import org.junit.Test

class AuthInterceptorTest {

    private val mockTokenProvider: TokenProvider = mockk()
    private val interceptor = AuthInterceptor(mockTokenProvider)

    @Test
    fun `intercept throws AuthorizationException when response code is 401`() {
        // Arrange
        every { mockTokenProvider.getToken() } returns "test-token"
        val request = Request.Builder().url("https://example.com/api").build()
        val response = buildResponse(request, 401, "Unauthorized")
        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response

        // Act / Assert
        assertThrows(AuthorizationException::class.java) {
            interceptor.intercept(chain)
        }
    }

    @Test
    fun `intercept throws AuthorizationException when response code is 403`() {
        // Arrange
        every { mockTokenProvider.getToken() } returns "test-token"
        val request = Request.Builder().url("https://example.com/api").build()
        val response = buildResponse(request, 403, "Forbidden")
        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response

        // Act / Assert
        assertThrows(AuthorizationException::class.java) {
            interceptor.intercept(chain)
        }
    }

    @Test
    fun `intercept returns response normally when response code is 200`() {
        // Arrange
        every { mockTokenProvider.getToken() } returns "test-token"
        val request = Request.Builder().url("https://example.com/api").build()
        val response = buildResponse(request, 200, "OK")
        val chain = mockk<Interceptor.Chain>()
        every { chain.request() } returns request
        every { chain.proceed(any()) } returns response

        // Act
        val result = interceptor.intercept(chain)

        // Assert
        assertEquals(200, result.code)
    }

    private fun buildResponse(request: Request, code: Int, message: String): Response =
        Response.Builder()
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message(message)
            .body("".toResponseBody(null))
            .build()
}
