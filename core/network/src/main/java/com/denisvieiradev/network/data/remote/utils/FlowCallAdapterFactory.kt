package com.denisvieiradev.network.data.remote.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class FlowCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Flow::class.java) return null
        val flowType = getParameterUpperBound(0, returnType as ParameterizedType)
        return FlowCallAdapter<Any>(flowType)
    }

    companion object {
        fun create(): FlowCallAdapterFactory = FlowCallAdapterFactory()
    }
}

private class FlowCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<T>> {
    override fun responseType(): Type = responseType

    override fun adapt(call: Call<T>): Flow<T> = flow {
        val response = call.execute()
        if (response.isSuccessful) {
            val body = response.body() ?: throw ApiException(response.code(), "Empty body")
            emit(body)
        } else {
            throw ApiException(response.code(), response.message())
        }
    }
}
