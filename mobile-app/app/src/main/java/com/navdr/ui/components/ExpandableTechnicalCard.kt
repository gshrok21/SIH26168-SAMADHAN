package com.navdr.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.model.NavigationUiState
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.StatusSuccessEmerald

@Composable
fun ExpandableTechnicalCard(
    state: NavigationUiState,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        border = BorderStroke(1.dp, CardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (state.positionMode.name == "DEAD_RECKONING") "Dead Reckoning Telemetry" else "System Telemetry & Telematics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Mode: ${state.positionMode.title} • Confidence: ${state.positionConfidence}%",
                        style = MaterialTheme.typography.labelSmall,
                        color = AccentCyan
                    )
                }
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand",
                    tint = Color.White
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(top = 14.dp)
                ) {
                    HorizontalDivider(color = CardBorderColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ESTIMATED METRICS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TechRow("Position Mode", state.positionMode.title)
                    TechRow("Latitude", state.latitude?.let { "%.6f°".format(it) } ?: "Waiting for GNSS")
                    TechRow("Longitude", state.longitude?.let { "%.6f°".format(it) } ?: "Waiting for GNSS")
                    TechRow("Estimated Accuracy", "±${state.accuracyMeters} m")
                    TechRow("Distance since GNSS Loss", state.distanceSinceGnssLost)
                    TechRow("Dead Reckoning Duration", state.drDuration)
                    TechRow("Sensor Status", state.sensorHealth.label, StatusSuccessEmerald)
                    TechRow("AI Position Fusion", "Not Active (AI/ML Pending)")

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = CardBorderColor)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "LIVE SENSOR STREAM (LIVE DATA)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = AccentCyan
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    TechRow("Accelerometer (m/s²)", "X: %.2f  Y: %.2f  Z: %.2f".format(state.accelX, state.accelY, state.accelZ))
                    TechRow("Gyroscope (rad/s)", "X: %.2f  Y: %.2f  Z: %.2f".format(state.gyroX, state.gyroY, state.gyroZ))
                    TechRow("Magnetometer (µT)", "X: %.1f  Y: %.1f  Z: %.1f".format(state.magX, state.magY, state.magZ))
                    TechRow("GNSS Satellites Lock", "${state.satellitesCount} satellites in view")
                    TechRow("AI Model Inference", "Not Active (AI/ML Pending)")
                }
            }
        }
    }
}

@Composable
private fun TechRow(
    label: String,
    value: String,
    valueColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1.2f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = valueColor,
            modifier = Modifier.weight(1.8f)
        )
    }
}
