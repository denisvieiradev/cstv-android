package com.denisvieiradev.cstv.ui.token.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.denisvieiradev.cstv.R

@Composable
internal fun TokenLogo() {
    Image(
        painter = painterResource(R.drawable.fuze_icon),
        contentDescription = null,
        modifier = Modifier.size(100.dp)
    )
}
