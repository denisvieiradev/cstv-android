package com.denisvieiradev.network.data.remote.utils

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken()
        if (token == null) {
            Timber.w("Request made without auth token: ${chain.request().url}")
        }
        val request = chain.request().newBuilder()
            .apply { if (token != null) header("Authorization", "Bearer $token") }
            .build()
        val response = chain.proceed(request)
        if (response.code == 401) {
            throw AuthorizationException(response.code)
        }
        return response
    }
}
