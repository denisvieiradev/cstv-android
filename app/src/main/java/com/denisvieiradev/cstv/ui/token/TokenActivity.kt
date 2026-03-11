package com.denisvieiradev.cstv.ui.token

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import com.denisvieiradev.cstv.ui.matches.MatchesActivity
import com.denisvieiradev.cstv.ui.token.model.TokenNavigationEvent
import org.koin.androidx.compose.koinViewModel

class TokenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: TokenViewModel = koinViewModel()
            LaunchedEffect(Unit) {
                viewModel.navigationEvents.collect { event ->
                    when (event) {
                        TokenNavigationEvent.RecreateActivity -> recreate()
                        TokenNavigationEvent.NavigateToMatches -> {
                            val intent = Intent(this@TokenActivity, MatchesActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
            TokenScreenRoot(viewModel = viewModel)
        }
    }
}
