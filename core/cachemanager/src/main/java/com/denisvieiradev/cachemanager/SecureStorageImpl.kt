package com.denisvieiradev.cachemanager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.io.IOException
import java.security.GeneralSecurityException

class SecureStorageImpl(context: Context) : SecureStorage {

    private val prefs: SharedPreferences = try {
        buildPrefs(context)
    } catch (e: GeneralSecurityException) {
        Log.e(TAG, "EncryptedSharedPreferences init failed, clearing prefs and retrying: ${e.message}")
        context.deleteSharedPreferences(PREFS_NAME)
        try {
            buildPrefs(context)
        } catch (e2: GeneralSecurityException) {
            Log.e(TAG, "EncryptedSharedPreferences retry failed, falling back to plain SharedPreferences: ${e2.message}")
            context.getSharedPreferences(PREFS_NAME_FALLBACK, Context.MODE_PRIVATE)
        } catch (e2: IOException) {
            Log.e(TAG, "EncryptedSharedPreferences retry failed, falling back to plain SharedPreferences: ${e2.message}")
            context.getSharedPreferences(PREFS_NAME_FALLBACK, Context.MODE_PRIVATE)
        }
    }

    override fun getString(key: String): String? = prefs.getString(key, null)
    override fun putString(key: String, value: String) = prefs.edit { putString(key, value) }
    override fun remove(key: String) = prefs.edit { remove(key) }

    companion object {
        private const val TAG = "SecureStorageImpl"
        private const val PREFS_NAME = "cstv_secure_prefs"
        private const val PREFS_NAME_FALLBACK = "cstv_prefs_fallback"

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
