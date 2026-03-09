package com.denisvieiradev.cstv.data.datasources.local

import com.denisvieiradev.cachemanager.SecureStorage

class SessionRepositoryImpl(private val secureStorage: SecureStorage) : SessionRepository {
    override fun getToken(): String? = secureStorage.getString(KEY_TOKEN)
    override fun saveToken(token: String) { secureStorage.putString(KEY_TOKEN, token) }
    override fun clearSession() { secureStorage.remove(KEY_TOKEN) }

    companion object {
        private const val KEY_TOKEN = "pandascore_token"
    }
}
