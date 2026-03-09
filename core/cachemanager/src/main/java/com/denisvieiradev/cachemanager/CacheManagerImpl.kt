package com.denisvieiradev.cachemanager

import com.orhanobut.hawk.Hawk

class CacheManagerImpl : CacheManager {
    override fun <T> get(key: String): T? = Hawk.get(key)
    override fun <T> put(key: String, value: T) { Hawk.put(key, value) }
    override fun delete(key: String) { Hawk.delete(key) }
}
