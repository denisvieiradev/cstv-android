package com.denisvieiradev.cstv.ui.matchdetail

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MatchDetailActivity : AppCompatActivity() {

    private val viewModel: MatchDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        collectNavigationEvents()
        setContent {
            MatchDetailScreenRoot(viewModel = viewModel)
        }
    }

    private fun collectNavigationEvents() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvents.collect { event ->
                    when (event) {
                        MatchDetailNavigationEvent.NavigateBack -> finish()
                    }
                }
            }
        }
    }
}
