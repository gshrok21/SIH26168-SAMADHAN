package com.navdr.ui.screens.demo

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
import com.navdr.model.DemoStage
import com.navdr.ui.components.AppTopBar
import com.navdr.ui.components.DemoControlCard
import com.navdr.ui.components.GnssStatusCard
import com.navdr.ui.components.PrimaryButton
import com.navdr.ui.components.StatusBanner
import com.navdr.ui.theme.DeepNavy
import com.navdr.viewmodel.NavigationViewModel

@Composable
fun DemoModeScreen(
    navigationViewModel: NavigationViewModel,
    onLaunchNavigationWithDemo: () -> Unit,
    onBackClick: () -> Unit
) {
    val navState by navigationViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "SIH 2026 Presentation Demo",
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
                text = "Live GNSS Loss & Dead Reckoning Simulator",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Demo Control Panel
            DemoControlCard(
                currentStage = navState.demoStage,
                onStageSelect = { stage ->
                    navigationViewModel.setDemoStage(stage)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Simulated Status Preview Card
            GnssStatusCard(
                gnssStatus = navState.gnssStatus,
                positionMode = navState.positionMode,
                accuracyMeters = navState.accuracyMeters,
                confidencePercent = navState.positionConfidence
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Status Banner Preview
            StatusBanner(
                gnssStatus = navState.gnssStatus,
                message = navState.bannerMessage
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = "Launch Live Navigation Demo",
                onClick = {
                    navigationViewModel.startNavigation("BHU Main Gate", "Varanasi")
                    onLaunchNavigationWithDemo()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
