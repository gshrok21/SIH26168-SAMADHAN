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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.DarkCardBg

@Composable
fun SensorStatusCard(
    sensorName: String,
    statusText: String,
    statusColor: Color,
    updateRateHz: Int? = null,
    accuracy: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        border = BorderStroke(1.dp, CardBorderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sensorName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (updateRateHz != null || accuracy != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = listOfNotNull(
                            updateRateHz?.let { "$it Hz rate" },
                            accuracy?.let { "Acc: $it" }
                        ).joinToString(" • "),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = "● $statusText",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = statusColor
            )
        }
    }
}
