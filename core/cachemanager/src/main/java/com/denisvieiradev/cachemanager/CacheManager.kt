package com.denisvieiradev.cachemanager

interface CacheManager {
    fun <T> get(key: String): T?
    fun <T> put(key: String, value: T)
    fun delete(key: String)
}