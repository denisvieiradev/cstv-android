package com.denisvieiradev.cstv.data.datasources.local

interface SessionRepository {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearSession()
}
