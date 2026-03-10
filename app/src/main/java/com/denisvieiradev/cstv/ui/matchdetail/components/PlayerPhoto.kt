package com.denisvieiradev.cstv.ui.matchdetail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.model.Player
import com.denisvieiradev.design_system.ui.theme.Spacing

@Composable
internal fun PlayerPhoto(player: Player?) {
    val photoSize = 48.dp
    val shape = RoundedCornerShape(Spacing.extraSmall)
    if (player == null) {
        PlayerInitialsBox("?", photoSize, shape)
        return
    }
    val initials = playerInitials(player.firstName, player.lastName, player.name)
    if (player.imageUrl != null) {
        SubcomposeAsyncImage(
            model = player.imageUrl,
            contentDescription = stringResource(R.string.match_detail_player_photo_desc, player.name),
            modifier = Modifier.size(photoSize).clip(shape),
            contentScale = ContentScale.Crop,
            error = { PlayerInitialsBox(initials, photoSize, shape) }
        )
    } else {
        PlayerInitialsBox(initials, photoSize, shape)
    }
}

@Composable
internal fun PlayerInitialsBox(initials: String, size: Dp, shape: Shape) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

internal fun playerInitials(firstName: String?, lastName: String?, name: String): String {
    val first = firstName?.firstOrNull()?.uppercaseChar()
    val last = lastName?.firstOrNull()?.uppercaseChar()
    return when {
        first != null && last != null -> "$first$last"
        first != null -> "$first"
        else -> name.take(1).uppercase()
    }
}
