package com.navdr.ui.screens.system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.ui.components.AppTopBar
import com.navdr.ui.components.BottomNavigationBar
import com.navdr.ui.components.ExpandableTechnicalCard
import com.navdr.ui.components.SensorStatusCard
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.DeepNavy
import com.navdr.ui.theme.StatusSuccessEmerald
import com.navdr.viewmodel.NavigationViewModel
import com.navdr.viewmodel.SystemViewModel

@Composable
fun SystemScreen(
    systemViewModel: SystemViewModel,
    navigationViewModel: NavigationViewModel,
    onBottomNavigate: (String) -> Unit,
    currentRoute: String
) {
    val sensors by systemViewModel.sensors.collectAsState()
    val navState by navigationViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(title = "System Health & Diagnostics")
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
                text = "Hardware Sensor Diagnostics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            sensors.forEach { sensor ->
                SensorStatusCard(
                    sensorName = sensor.name,
                    statusText = sensor.status,
                    statusColor = if (sensor.name.contains("AI")) AccentCyan else StatusSuccessEmerald,
                    updateRateHz = sensor.updateRateHz,
                    accuracy = sensor.accuracy
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Technical Telemetry Stream",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(10.dp))

            ExpandableTechnicalCard(state = navState)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
