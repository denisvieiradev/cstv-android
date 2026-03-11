package com.denisvieiradev.cstv.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.denisvieiradev.cstv.ui.matches.MatchesActivity
import com.denisvieiradev.cstv.ui.token.TokenActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {

    private val viewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        collectNavigationEvents()
        viewModel.onAction(SplashScreenAction.CheckSession)
    }

    private fun collectNavigationEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvents.collect { event ->
                    when (event) {
                        SplashScreenNavigationEvent.NavigateToMatches -> navigateToMatches()
                        SplashScreenNavigationEvent.NavigateToToken -> navigateToToken()
                    }
                }
            }
        }
    }

    private fun navigateToMatches() {
        startActivity(Intent(this, MatchesActivity::class.java))
        finish()
    }

    private fun navigateToToken() {
        startActivity(Intent(this, TokenActivity::class.java))
        finish()
    }
}
