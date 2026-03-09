package com.denisvieiradev.cstv.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.denisvieiradev.cachemanager.SessionRepository
import com.denisvieiradev.cstv.ui.matches.MatchesActivity
import com.denisvieiradev.cstv.ui.token.TokenActivity
import org.koin.android.ext.android.inject

class SplashScreenActivity : AppCompatActivity() {

    private val sessionRepository: SessionRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        routeToNextScreen()
    }

    private fun routeToNextScreen() {
        val destination = if (sessionRepository.getToken() != null) {
            Intent(this, MatchesActivity::class.java)
        } else {
            Intent(this, TokenActivity::class.java)
        }
        startActivity(destination)
        finish()
    }
}
