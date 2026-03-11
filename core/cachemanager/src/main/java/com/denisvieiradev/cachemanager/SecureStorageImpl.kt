package com.denisvieiradev.cachemanager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorageImpl(context: Context) : SecureStorage {

    private val prefs: SharedPreferences = try {
        buildPrefs(context)
    } catch (e: Exception) {
        Log.w(TAG, "EncryptedSharedPreferences init failed, clearing prefs and retrying: ${e.message}")
        context.deleteSharedPreferences(PREFS_NAME)
        buildPrefs(context)
    }

    override fun getString(key: String): String? = prefs.getString(key, null)
    override fun putString(key: String, value: String) = prefs.edit { putString(key, value) }
    override fun remove(key: String) = prefs.edit { remove(key) }

    companion object {
        private const val TAG = "SecureStorageImpl"
        private const val PREFS_NAME = "cstv_secure_prefs"

        private fun buildPrefs(context: Context): SharedPreferences =
            EncryptedSharedPreferences.create(
                context,
                PREFS_NAME,
                MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }
}
