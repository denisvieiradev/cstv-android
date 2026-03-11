package com.denisvieiradev.cstv.core

import android.app.Application
import com.denisvieiradev.cstv.BuildConfig
import com.denisvieiradev.cstv.core.di.getModules
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.domain.Language
import com.denisvieiradev.cstv.ui.matches.LocaleManager
import com.denisvieiradev.cstv.ui.matches.ThemeManager
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.Locale

class App : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        Hawk.init(this).build()

        startKoin {
            androidContext(this@App)
            modules(getModules)
        }

        applyInitialLocale()
        applyInitialNightMode()
    }

    private fun applyInitialNightMode() {
        val sessionLocalDataSource: SessionLocalDataSource by inject()
        val themeManager: ThemeManager by inject()
        themeManager.apply(sessionLocalDataSource.isDarkTheme())
    }

    private fun applyInitialLocale() {
        val sessionLocalDataSource: SessionLocalDataSource by inject()
        val localeManager: LocaleManager by inject()
        val saved = sessionLocalDataSource.getLanguage()
        val tag = if (saved != null) {
            if (saved == "pt") Language.PT.also { sessionLocalDataSource.saveLanguage(it) } else saved
        } else {
            val systemLang = Locale.getDefault().language
            val resolved = if (systemLang == "pt") Language.PT else Language.EN
            sessionLocalDataSource.saveLanguage(resolved)
            resolved
        }
        localeManager.apply(tag)
    }
}
