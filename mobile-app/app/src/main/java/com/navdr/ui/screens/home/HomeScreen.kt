package com.navdr.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.model.Trip
import com.navdr.ui.components.AppTopBar
import com.navdr.ui.components.BottomNavigationBar
import com.navdr.ui.components.GnssStatusCard
import com.navdr.ui.components.PrimaryButton
import com.navdr.ui.components.SecondaryButton
import com.navdr.ui.components.SystemHealthCard
import com.navdr.ui.components.TripCard
import com.navdr.ui.theme.DeepNavy
import com.navdr.viewmodel.HomeViewModel
import com.navdr.viewmodel.NavigationViewModel

@Composable
fun HomeScreen(
    navigationViewModel: NavigationViewModel,
    homeViewModel: HomeViewModel,
    onStartNavigationClick: () -> Unit,
    onRecentTripClick: (Trip) -> Unit,
    onSettingsClick: () -> Unit,
    onBottomNavigate: (String) -> Unit,
    currentRoute: String
) {
    val navState by navigationViewModel.uiState.collectAsState()
    val recentTrips by homeViewModel.recentTrips.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "NavDR",
                onSettingsClick = onSettingsClick
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = onBottomNavigate
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
                text = "Good afternoon,",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Ready to navigate?",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Main Status Card
            GnssStatusCard(
                gnssStatus = navState.gnssStatus,
                positionMode = navState.positionMode,
                accuracyMeters = navState.accuracyMeters,
                confidencePercent = navState.positionConfidence
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Actions
            PrimaryButton(
                text = "Start Navigation",
                icon = Icons.Default.Navigation,
                onClick = onStartNavigationClick
            )

            Spacer(modifier = Modifier.height(10.dp))

            SecondaryButton(
                text = "View Recent Trips",
                onClick = {
                    recentTrips.firstOrNull()?.let { onRecentTripClick(it) }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // System Health
            SystemHealthCard()

            Spacer(modifier = Modifier.height(24.dp))

            // Recent Routes
            Text(
                text = "Recent Routes",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            recentTrips.take(3).forEach { trip ->
                TripCard(
                    trip = trip,
                    onClick = { onRecentTripClick(trip) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
