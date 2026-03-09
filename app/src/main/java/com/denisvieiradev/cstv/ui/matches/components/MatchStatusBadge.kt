package com.denisvieiradev.cstv.ui.matches.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.denisvieiradev.cstv.R
import com.denisvieiradev.cstv.domain.model.MatchStatus
import com.denisvieiradev.cstv.ui.matches.util.MatchDateFormatter
import com.denisvieiradev.design_system.ui.theme.Spacing
import java.time.LocalDate

@Composable
fun MatchStatusBadge(
    status: MatchStatus,
    scheduledAt: String?,
    modifier: Modifier = Modifier
) {
    if (status == MatchStatus.RUNNING) {
        Box(
            modifier = modifier
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomStart = 8.dp))
                .background(MaterialTheme.colorScheme.error)
                .padding(horizontal = Spacing.small, vertical = 4.dp)
        ) {
            Text(
                text = stringResource(R.string.match_status_live),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onError
            )
        }
    } else {
        val formattedDate = MatchDateFormatter.format(
            scheduledAt = scheduledAt,
            now = LocalDate.now()
        )
        Box(
            modifier = modifier.padding(horizontal = Spacing.small, vertical = 4.dp)
        ) {
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
