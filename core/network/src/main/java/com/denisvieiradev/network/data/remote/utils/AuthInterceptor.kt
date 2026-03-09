package com.denisvieiradev.network.data.remote.utils

import com.denisvieiradev.cachemanager.SessionRepository
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val sessionRepository: SessionRepository) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = sessionRepository.getToken()
        val request = chain.request().newBuilder()
            .apply { if (token != null) header("Authorization", "Bearer $token") }
            .build()
        return chain.proceed(request)
    }
}
