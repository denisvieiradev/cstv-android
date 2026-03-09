package com.denisvieiradev.cachemanager

import com.orhanobut.hawk.Hawk

class SessionRepositoryImpl : SessionRepository {

    override fun getToken(): String? = Hawk.get(KEY_TOKEN)

    override fun saveToken(token: String) {
        Hawk.put(KEY_TOKEN, token)
    }

    override fun clearSession() {
        Hawk.delete(KEY_TOKEN)
    }

    companion object {
        private const val KEY_TOKEN = "pandascore_token"
    }
}
