package com.denisvieiradev.cstv.core

import android.app.Application
import com.denisvieiradev.cstv.core.di.getModules
import com.orhanobut.hawk.Hawk
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        Hawk.init(this).build()

        startKoin {
            androidContext(this@App)
            modules(getModules)
        }
    }
}
