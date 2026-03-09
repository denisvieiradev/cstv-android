package com.denisvieiradev.cstv.core

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.denisvieiradev.cstv.BuildConfig
import com.denisvieiradev.cstv.core.di.getModules
import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.denisvieiradev.cstv.domain.Language
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
    }

    private fun applyInitialLocale() {
        val sessionRepository: SessionRepository by inject()
        val saved = sessionRepository.getLanguage()
        val tag = if (saved != null) {
            saved
        } else {
            val systemLang = Locale.getDefault().language
            val resolved = if (systemLang == "pt") Language.PT else Language.EN
            sessionRepository.saveLanguage(resolved)
            resolved
        }
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
    }
}
