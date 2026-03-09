package com.denisvieiradev.cachemanager

interface SecureStorage {
    fun getString(key: String): String?
    fun putString(key: String, value: String)
    fun remove(key: String)
}
