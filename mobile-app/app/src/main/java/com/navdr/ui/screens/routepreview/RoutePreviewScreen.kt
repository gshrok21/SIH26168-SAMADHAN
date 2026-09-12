package com.navdr.ui.screens.routepreview

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.navdr.ui.components.AppTopBar
import com.navdr.ui.components.MockMapCanvas
import com.navdr.ui.components.PrimaryButton
import com.navdr.ui.components.SecondaryButton
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.DeepNavy
import com.navdr.ui.theme.PrimaryBlue
import com.navdr.viewmodel.NavigationViewModel

@Composable
fun RoutePreviewScreen(
    navigationViewModel: NavigationViewModel,
    destinationName: String,
    destinationArea: String,
    onStartNavigation: () -> Unit,
    onBackClick: () -> Unit
) {
    val navState by navigationViewModel.uiState.collectAsState()
    var selectedRouteType by remember { mutableStateOf("Fastest") }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Route Preview",
                onBackClick = onBackClick
            )
        },
        containerColor = DeepNavy
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Map Preview Canvas Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                MockMapCanvas(
                    gnssStatus = navState.gnssStatus,
                    positionMode = navState.positionMode,
                    confidence = navState.positionConfidence,
                    isNavigating = false,
                    destinationName = destinationName
                )
            }

            // Bottom Route Control Panel
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                border = BorderStroke(1.dp, CardBorderColor)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = destinationName,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "$destinationArea • Estimated arrival 12:45 PM",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Text(
                            text = "4.8 km\n12 min",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Route Type Options (Fastest / Recommended)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
                    ) {
                        RouteOptionChip(
                            title = "Fastest Route",
                            subtitle = "4.8 km • 12 min",
                            isSelected = selectedRouteType == "Fastest",
                            onClick = { selectedRouteType = "Fastest" },
                            modifier = Modifier.weight(1f)
                        )

                        RouteOptionChip(
                            title = "Recommended",
                            subtitle = "5.2 km • 14 min",
                            isSelected = selectedRouteType == "Recommended",
                            onClick = { selectedRouteType = "Recommended" },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Navigation Transport Modes (Driving, Walking, Cycling)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceAround
                    ) {
                        TransportModeButton(
                            icon = Icons.Default.DirectionsCar,
                            label = "Driving",
                            isSelected = navState.navigationMode == "Driving",
                            onClick = { navigationViewModel.setNavigationMode("Driving") }
                        )
                        TransportModeButton(
                            icon = Icons.Default.TwoWheeler,
                            label = "Cycling",
                            isSelected = navState.navigationMode == "Cycling",
                            onClick = { navigationViewModel.setNavigationMode("Cycling") }
                        )
                        TransportModeButton(
                            icon = Icons.Default.DirectionsWalk,
                            label = "Walking",
                            isSelected = navState.navigationMode == "Walking",
                            onClick = { navigationViewModel.setNavigationMode("Walking") }
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    PrimaryButton(
                        text = "Start Navigation",
                        icon = Icons.Default.Navigation,
                        onClick = {
                            navigationViewModel.startNavigation(destinationName, destinationArea)
                            onStartNavigation()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RouteOptionChip(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) PrimaryBlue.copy(alpha = 0.2f) else DeepNavy
        ),
        border = BorderStroke(
            1.5.dp,
            if (isSelected) PrimaryBlue else CardBorderColor
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) AccentCyan else Color.White
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TransportModeButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) AccentCyan else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) AccentCyan else MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}
