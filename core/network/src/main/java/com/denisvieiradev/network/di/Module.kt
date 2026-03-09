package com.denisvieiradev.network.di

import com.denisvieiradev.network.BuildConfig
import com.denisvieiradev.network.data.constants.ApiConstants
import com.denisvieiradev.network.data.remote.utils.AuthInterceptor
import com.denisvieiradev.network.data.remote.utils.SimpleBasicOkHttpClient
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single { AuthInterceptor(get()) }

    single {
        SimpleBasicOkHttpClient.build(
            authInterceptor = get(),
            isDebug = BuildConfig.DEBUG
        )
    }

    single {
        Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
