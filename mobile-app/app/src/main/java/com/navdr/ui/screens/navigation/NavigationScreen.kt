package com.navdr.ui.screens.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.model.DemoStage
import com.navdr.model.GnssStatus
import com.navdr.ui.components.ConfidenceIndicator
import com.navdr.ui.components.DemoControlCard
import com.navdr.ui.components.ExpandableTechnicalCard
import com.navdr.ui.components.MockMapCanvas
import com.navdr.ui.components.NavigationInstructionCard
import com.navdr.ui.components.StatusBanner
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.DeepNavy
import com.navdr.ui.theme.StatusDangerRed
import com.navdr.ui.theme.StatusSuccessEmerald
import com.navdr.ui.theme.StatusWarningAmber
import com.navdr.viewmodel.NavigationViewModel

@Composable
fun NavigationScreen(
    navigationViewModel: NavigationViewModel,
    onExitNavigation: () -> Unit,
    onOpenDemoPanel: () -> Unit
) {
    val state by navigationViewModel.uiState.collectAsState()
    var showDemoControls by remember { mutableStateOf(false) }

    val gnssColor = when (state.gnssStatus) {
        GnssStatus.GNSS_AVAILABLE, GnssStatus.GNSS_RECOVERED -> StatusSuccessEmerald
        GnssStatus.GNSS_WEAK -> StatusWarningAmber
        GnssStatus.GNSS_LOST -> StatusDangerRed
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy)
    ) {
        // 1. FULL SCREEN INTERACTIVE CANVAS MAP
        MockMapCanvas(
            gnssStatus = state.gnssStatus,
            positionMode = state.positionMode,
            confidence = state.positionConfidence,
            isNavigating = true,
            destinationName = state.destinationName,
            modifier = Modifier.fillMaxSize()
        )

        // 2. OVERLAY CONTROLS COLUMN
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // TOP BAR OVERLAY CARD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardBg.copy(alpha = 0.92f)),
                border = BorderStroke(1.dp, CardBorderColor)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            navigationViewModel.stopNavigation()
                            onExitNavigation()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Navigation",
                            tint = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = state.destinationName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${state.distanceRemaining} • ${state.durationRemaining} left",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // GNSS Status Icon + Label + Color
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = gnssColor.copy(alpha = 0.2f)),
                        border = BorderStroke(1.dp, gnssColor)
                    ) {
                        Text(
                            text = state.gnssStatus.displayIconText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = gnssColor,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // STATUS BANNER (GNSS LOST / RECOVERED / WEAK)
            StatusBanner(
                gnssStatus = state.gnssStatus,
                message = state.bannerMessage
            )

            Spacer(modifier = Modifier.height(10.dp))

            // NAVIGATION INSTRUCTION CARD
            NavigationInstructionCard(
                distanceToTurn = state.distanceToNextTurn,
                instructionText = state.currentInstruction,
                roadName = state.currentRoad
            )

            Spacer(modifier = Modifier.weight(1f))

            // FLOATING ACTION BUTTONS (Demo Control Toggle & Recenter)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                // Demo Mode Trigger
                Card(
                    modifier = Modifier.clickable { showDemoControls = !showDemoControls },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentCyan.copy(alpha = 0.2f)),
                    border = BorderStroke(1.5.dp, AccentCyan)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(imageVector = Icons.Default.DeveloperMode, contentDescription = "Demo Mode", tint = AccentCyan)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("DEMO SIMULATOR", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = AccentCyan)
                    }
                }

                // Recenter location button
                Card(
                    modifier = Modifier.clickable { /* Recenter map */ },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Box(modifier = Modifier.padding(10.dp)) {
                        Icon(imageVector = Icons.Default.MyLocation, contentDescription = "Center", tint = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // DEMO CONTROL CARD OVERLAY IF TOGGLED
            if (showDemoControls) {
                DemoControlCard(
                    currentStage = state.demoStage,
                    onStageSelect = { stage ->
                        navigationViewModel.setDemoStage(stage)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // EXPANDABLE TELEMETRY CARD AT BOTTOM
            ExpandableTechnicalCard(state = state)
        }
    }
}
