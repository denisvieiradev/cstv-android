package com.denisvieiradev.cstv.data.datasources.local

interface SessionLocalDataSource {
    fun getToken(): String?
    fun saveToken(token: String)
    fun clearSession()
    fun isDarkTheme(): Boolean
    fun saveDarkTheme(isDark: Boolean)
    fun getLanguage(): String?
    fun saveLanguage(languageTag: String)
    fun isDemoUsed(): Boolean
    fun saveDemoUsed()
}
