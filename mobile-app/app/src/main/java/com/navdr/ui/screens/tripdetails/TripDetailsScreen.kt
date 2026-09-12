package com.navdr.ui.screens.tripdetails

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.navdr.model.GnssStatus
import com.navdr.model.PositionMode
import com.navdr.model.TimelineEvent
import com.navdr.model.Trip
import com.navdr.ui.components.AppTopBar
import com.navdr.ui.components.MetricCard
import com.navdr.ui.components.MockMapCanvas
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.CardBorderColor
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.DeepNavy
import com.navdr.ui.theme.PrimaryBlue
import com.navdr.ui.theme.StatusSuccessEmerald

@Composable
fun TripDetailsScreen(
    trip: Trip?,
    onBackClick: () -> Unit
) {
    val currentTrip = trip ?: return

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Trip Analysis • ${currentTrip.destination}",
                onBackClick = onBackClick
            )
        },
        containerColor = DeepNavy
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Map Replay Canvas Preview
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                MockMapCanvas(
                    gnssStatus = GnssStatus.GNSS_RECOVERED,
                    positionMode = PositionMode.DEAD_RECKONING,
                    confidence = 82,
                    isNavigating = false,
                    destinationName = currentTrip.destination
                )
            }

            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "${currentTrip.origin} → ${currentTrip.destination}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = currentTrip.date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Trip Summary Metrics Grid
                Row(modifier = Modifier.fillMaxWidth()) {
                    MetricCard(
                        label = "Distance",
                        value = "${currentTrip.totalDistanceKm} km",
                        valueColor = PrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    MetricCard(
                        label = "Duration",
                        value = "${currentTrip.durationMinutes} min",
                        valueColor = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    MetricCard(
                        label = "Avg Speed",
                        value = "${currentTrip.avgSpeedKmh} km/h",
                        valueColor = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    MetricCard(
                        label = "GNSS Coverage",
                        value = "${currentTrip.gnssCoveragePercent}%",
                        valueColor = StatusSuccessEmerald,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    MetricCard(
                        label = "Dead Reckoning",
                        value = "${currentTrip.deadReckoningDistanceKm} km",
                        valueColor = AccentCyan,
                        subtitle = "${100 - currentTrip.gnssCoveragePercent}% total journey",
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    MetricCard(
                        label = "Max Est. Error",
                        value = "±${currentTrip.maxEstimatedErrorMeters.toInt()} m",
                        valueColor = Color.White,
                        subtitle = "During signal loss",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Positioning Timeline
                Text(
                    text = "POSITIONING TIMELINE",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    border = BorderStroke(1.dp, CardBorderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        currentTrip.timeline.forEachIndexed { index, event ->
                            TimelineRow(event = event, isLast = index == currentTrip.timeline.size - 1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TimelineRow(
    event: TimelineEvent,
    isLast: Boolean
) {
    val parseColor = try {
        Color(android.graphics.Color.parseColor(event.statusColorHex))
    } catch (e: Exception) {
        StatusSuccessEmerald
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(28.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .background(parseColor, CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(44.dp)
                        .background(CardBorderColor)
                )
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = event.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = parseColor,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = event.timeOffset,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = event.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
