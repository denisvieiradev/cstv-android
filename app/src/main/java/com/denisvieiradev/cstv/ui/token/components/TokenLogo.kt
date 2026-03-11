package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.denisvieiradev.cstv.R

@Composable
internal fun TokenLogo(logoGradient: Brush) {
    Box(
        modifier = Modifier
            .size(100.dp)
            .graphicsLayer { alpha = 0.99f }
    ) {
        Image(
            painter = painterResource(R.drawable.fuze_icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize()
        )
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(brush = logoGradient, blendMode = BlendMode.SrcAtop)
        }
    }
}
