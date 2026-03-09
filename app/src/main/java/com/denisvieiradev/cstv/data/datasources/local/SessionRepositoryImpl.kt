package com.denisvieiradev.cstv.data.datasources.local

import com.denisvieiradev.cachemanager.CacheManager

class SessionRepositoryImpl(private val cacheManager: CacheManager) : SessionRepository {
    override fun getToken(): String? = cacheManager.get(KEY_TOKEN)
    override fun saveToken(token: String) { cacheManager.put(KEY_TOKEN, token) }
    override fun clearSession() { cacheManager.delete(KEY_TOKEN) }

    companion object {
        private const val KEY_TOKEN = "pandascore_token"
    }
}
