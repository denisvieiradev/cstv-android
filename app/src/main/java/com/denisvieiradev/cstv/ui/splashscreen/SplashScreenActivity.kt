package com.denisvieiradev.cstv.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.denisvieiradev.cstv.data.datasources.local.SessionLocalDataSource
import com.denisvieiradev.cstv.ui.matches.MatchesActivity
import com.denisvieiradev.cstv.ui.token.TokenActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class SplashScreenActivity : AppCompatActivity() {

    private val sessionLocalDataSource: SessionLocalDataSource by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            routeToNextScreen()
        }
    }

    private suspend fun routeToNextScreen() {
        val hasToken = withContext(Dispatchers.IO) { sessionLocalDataSource.getToken() != null }
        val destination = if (hasToken) {
            Intent(this, MatchesActivity::class.java)
        } else {
            Intent(this, TokenActivity::class.java)
        }
        startActivity(destination)
        finish()
    }
}
