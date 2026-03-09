package com.denisvieiradev.network.data.remote.utils

fun interface TokenProvider {
    fun getToken(): String?
}
