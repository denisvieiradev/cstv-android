package com.denisvieiradev.cstv.ui.token

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import com.denisvieiradev.cstv.data.datasources.local.SessionRepository
import com.denisvieiradev.cstv.ui.token.components.TokenScreenRoot
import com.denisvieiradev.design_system.ui.theme.CstvTheme
import org.koin.android.ext.android.inject

class TokenActivity : AppCompatActivity() {

    private val sessionRepository: SessionRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CstvTheme(darkTheme = sessionRepository.isDarkTheme()) {
                TokenScreenRoot()
            }
        }
    }
}
