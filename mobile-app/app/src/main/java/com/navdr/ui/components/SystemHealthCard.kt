package com.navdr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.StatusSuccessEmerald

@Composable
fun SystemHealthCard(
    gnssStatusText: String = "Strong",
    sensorStatusText: String = "Healthy",
    aiStatusText: String = "Ready",
    batteryText: String = "82%",
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "System Health",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            CompactHealthBox("GNSS", gnssStatusText, StatusSuccessEmerald, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(0.dp).padding(horizontal = 4.dp))
            CompactHealthBox("Sensors", sensorStatusText, StatusSuccessEmerald, modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            CompactHealthBox("AI Positioning", aiStatusText, AccentCyan, modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(0.dp).padding(horizontal = 4.dp))
            CompactHealthBox("Battery", batteryText, Color.White, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun CompactHealthBox(
    label: String,
    status: String,
    statusColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        border = BorderStroke(1.dp, CardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "● $status",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        }
    }
}
