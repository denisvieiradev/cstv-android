package com.denisvieiradev.cstv.ui.matches

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.denisvieiradev.cstv.ui.matches.components.MatchesScreenRoot
import com.denisvieiradev.cstv.ui.token.TokenActivity
import com.denisvieiradev.design_system.ui.theme.CstvTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchesActivity : ComponentActivity() {

    private val viewModel: MatchesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        collectNavigationEvents()
        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            CstvTheme(darkTheme = uiState.isDarkTheme) {
                MatchesScreenRoot(viewModel = viewModel)
            }
        }
    }

    private fun collectNavigationEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvents.collect { event ->
                    when (event) {
                        MatchesNavigationEvent.NavigateToTokenScreen -> navigateToTokenScreen()
                    }
                }
            }
        }
    }

    private fun navigateToTokenScreen() {
        startActivity(Intent(this, TokenActivity::class.java))
        finish()
    }
}
