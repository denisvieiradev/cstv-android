package com.denisvieiradev.network.data.remote.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

private const val TIMEOUT_SECONDS = 30L
private const val HEADER_AUTHORIZATION = "Authorization"

object SimpleBasicOkHttpClient {
    fun build(
        authInterceptor: AuthInterceptor,
        isDebug: Boolean
    ): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(authInterceptor)
        .apply {
            if (isDebug) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                        redactHeader(HEADER_AUTHORIZATION)
                    }
                )
            }
        }
        .build()
}
