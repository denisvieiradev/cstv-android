package com.denisvieiradev.network.data.remote.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val tokenProvider: TokenProvider) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenProvider.getToken()
        val request = chain.request().newBuilder()
            .apply { if (token != null) header("Authorization", "Bearer $token") }
            .build()
        val response = chain.proceed(request)
        if (response.code in listOf(401, 403)) {
            Log.d("AuthInterceptor", "Auth error detected: HTTP ${response.code}")
            throw AuthorizationException(response.code)
        }
        return response
    }
}
