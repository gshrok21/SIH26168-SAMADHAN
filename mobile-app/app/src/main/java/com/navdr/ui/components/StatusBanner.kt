package com.navdr.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.model.GnssStatus
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.StatusDangerRed
import com.navdr.ui.theme.StatusSuccessEmerald
import com.navdr.ui.theme.StatusWarningAmber

@Composable
fun StatusBanner(
    gnssStatus: GnssStatus,
    message: String?,
    modifier: Modifier = Modifier
) {
    val visible = message != null || gnssStatus == GnssStatus.GNSS_LOST || gnssStatus == GnssStatus.GNSS_RECOVERED

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
        modifier = modifier
    ) {
        val (bannerTitle, bannerSubtitle, accentColor) = when (gnssStatus) {
            GnssStatus.GNSS_LOST -> Triple(
                "GNSS Signal Lost • Dead Reckoning Active",
                "Navigation continuing using motion sensors and intelligent positioning.",
                StatusWarningAmber
            )
            GnssStatus.GNSS_RECOVERED -> Triple(
                "GNSS Signal Recovered • Position Synchronized",
                "Satellite constellation re-acquired. Position telemetry synced.",
                StatusSuccessEmerald
            )
            GnssStatus.GNSS_WEAK -> Triple(
                "GNSS Signal Weak",
                "Satellite signal accuracy degraded. AI fusion monitoring sensors.",
                StatusWarningAmber
            )
            GnssStatus.GNSS_AVAILABLE -> Triple(
                message ?: "GNSS Signal Strong",
                "Normal satellite navigation active.",
                StatusSuccessEmerald
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkCardBg.copy(alpha = 0.95f)),
            border = BorderStroke(1.5.dp, accentColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (gnssStatus == GnssStatus.GNSS_LOST) "⚠" else "✓",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = bannerTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = bannerSubtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
