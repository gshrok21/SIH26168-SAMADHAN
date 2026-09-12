package com.navdr.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.model.DemoStage
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.StatusDangerRed
import com.navdr.ui.theme.StatusSuccessEmerald
import com.navdr.ui.theme.StatusWarningAmber

@Composable
fun DemoControlCard(
    currentStage: DemoStage,
    onStageSelect: (DemoStage) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkCardBg),
        border = BorderStroke(1.dp, AccentCyan.copy(alpha = 0.6f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "DEMO SIMULATOR",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AccentCyan
                )
                Text(
                    text = "SIH Presentation Mode",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Simulate GNSS conditions to demonstrate seamless dead-reckoning navigation transition.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onStageSelect(DemoStage.NORMAL) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStage == DemoStage.NORMAL) StatusSuccessEmerald else DarkCardBg
                    ),
                    border = BorderStroke(1.dp, StatusSuccessEmerald),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("GNSS Strong", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onStageSelect(DemoStage.WEAK_SIGNAL) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStage == DemoStage.WEAK_SIGNAL) StatusWarningAmber else DarkCardBg
                    ),
                    border = BorderStroke(1.dp, StatusWarningAmber),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("GNSS Weak", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onStageSelect(DemoStage.GNSS_LOST) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStage == DemoStage.GNSS_LOST || currentStage == DemoStage.DEAD_RECKONING) StatusDangerRed else DarkCardBg
                    ),
                    border = BorderStroke(1.dp, StatusDangerRed),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Simulate Loss", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = { onStageSelect(DemoStage.GNSS_RECOVERED) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentStage == DemoStage.GNSS_RECOVERED) StatusSuccessEmerald else DarkCardBg
                    ),
                    border = BorderStroke(1.dp, StatusSuccessEmerald),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Restore GNSS", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
