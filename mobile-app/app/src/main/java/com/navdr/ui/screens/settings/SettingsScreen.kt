package com.navdr.ui.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.navdr.ui.components.AppTopBar
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.DeepNavy
import com.navdr.ui.theme.PrimaryBlue

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    forceDeadReckoning: Boolean,
    onForceDeadReckoningChange: (Boolean) -> Unit
) {
    var voiceGuidance by remember { mutableStateOf(true) }
    var autoReroute by remember { mutableStateOf(true) }
    var batterySaver by remember { mutableStateOf(false) }
    var demoModeEnabled by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Settings",
                onBackClick = onBackClick
            )
        },
        containerColor = DeepNavy
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Navigation Preferences",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkCardBg
                ),
                border = BorderStroke(
                    1.dp,
                    CardBorderColor
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    SettingToggleRow(
                        title = "Voice Guidance",
                        subtitle = "Spoken turn-by-turn navigation alerts",
                        checked = voiceGuidance,
                        onCheckedChange = {
                            voiceGuidance = it
                        }
                    )

                    HorizontalDivider(
                        color = CardBorderColor,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    SettingToggleRow(
                        title = "Auto Re-route",
                        subtitle = "Automatically recalculate path if turn missed",
                        checked = autoReroute,
                        onCheckedChange = {
                            autoReroute = it
                        }
                    )

                    HorizontalDivider(
                        color = CardBorderColor,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    SettingToggleRow(
                        title = "Battery Saver Mode",
                        subtitle = "Reduce sensor sampling rate when battery is low",
                        checked = batterySaver,
                        onCheckedChange = {
                            batterySaver = it
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "System & Presentation",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DarkCardBg
                ),
                border = BorderStroke(
                    1.dp,
                    CardBorderColor
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    SettingToggleRow(
                        title = "SIH Presentation Demo Mode",
                        subtitle = "Expose simulation tools on navigation screen",
                        checked = demoModeEnabled,
                        onCheckedChange = {
                            demoModeEnabled = it
                        }
                    )

                    HorizontalDivider(
                        color = CardBorderColor,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    SettingToggleRow(
                        title = "Force Dead Reckoning",
                        subtitle = "Testing mode — ignore live GNSS updates",
                        checked = forceDeadReckoning,
                        onCheckedChange = { enabled ->
                            onForceDeadReckoningChange(enabled)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryBlue,
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                uncheckedTrackColor = DeepNavy
            )
        )
    }
}