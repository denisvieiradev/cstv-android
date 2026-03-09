package com.denisvieiradev.cachemanager

interface SessionRepository {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearSession()
}
