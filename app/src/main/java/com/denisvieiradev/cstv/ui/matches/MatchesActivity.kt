package com.denisvieiradev.cstv.ui.matches

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.denisvieiradev.cstv.ui.navigation.MatchesNavHost
import com.denisvieiradev.cstv.ui.token.TokenActivity

class MatchesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            MatchesNavHost(
                navController = navController,
                onNavigateToTokenScreen = {
                    startActivity(Intent(this@MatchesActivity, TokenActivity::class.java))
                    finishAffinity()
                },
                onRecreateActivity = { recreate() }
            )
        }
    }
}
