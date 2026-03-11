package com.denisvieiradev.cstv.ui.matches

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.denisvieiradev.cstv.ui.matchdetail.MatchDetailActivity
import com.denisvieiradev.cstv.ui.matchdetail.MatchDetailViewModel
import com.denisvieiradev.cstv.ui.token.TokenActivity
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchesActivity : AppCompatActivity() {

    private val viewModel: MatchesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        collectNavigationEvents()
        setContent {
            MatchesScreenRoot(viewModel = viewModel)
        }
    }

    private fun collectNavigationEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvents.collect { event ->
                    when (event) {
                        MatchesNavigationEvent.NavigateToTokenScreen -> navigateToTokenScreen()
                        MatchesNavigationEvent.RecreateActivity -> recreate()
                        is MatchesNavigationEvent.OpenMatchDetail -> {
                            val intent = Intent(this@MatchesActivity, MatchDetailActivity::class.java)
                            intent.putExtra(MatchDetailViewModel.EXTRA_MATCH, event.match)
                            startActivity(intent)
                        }
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
