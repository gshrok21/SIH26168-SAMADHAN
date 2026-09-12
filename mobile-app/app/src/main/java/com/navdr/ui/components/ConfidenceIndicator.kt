package com.navdr.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.StatusDangerRed
import com.navdr.ui.theme.StatusSuccessEmerald
import com.navdr.ui.theme.StatusWarningAmber

@Composable
fun ConfidenceIndicator(
    confidence: Int,
    modifier: Modifier = Modifier
) {
    val (label, color, icon) = when {
        confidence >= 90 -> Triple("High Confidence", StatusSuccessEmerald, "✓ High")
        confidence >= 75 -> Triple("Moderate Confidence", AccentCyan, "⚡ Moderate")
        confidence >= 60 -> Triple("Acceptable DR", StatusWarningAmber, "⚠ Moderate")
        else -> Triple("Low Confidence", StatusDangerRed, "× Low")
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Position Confidence",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "$confidence% ($icon)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        LinearProgressIndicator(
            progress = { confidence / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = CardBorderColor,
        )
    }
}
