package com.denisvieiradev.network.data.remote.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object SimpleBasicOkHttpClient {
    fun build(
        authInterceptor: AuthInterceptor,
        isDebug: Boolean
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .apply {
            if (isDebug) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
            }
        }
        .build()
}
