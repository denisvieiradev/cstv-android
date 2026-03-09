package com.denisvieiradev.cstv.ui.token

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.denisvieiradev.cstv.ui.token.components.TokenScreenRoot
import com.denisvieiradev.design_system.ui.theme.CstvTheme

class TokenActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CstvTheme {
                TokenScreenRoot()
            }
        }
    }
}
