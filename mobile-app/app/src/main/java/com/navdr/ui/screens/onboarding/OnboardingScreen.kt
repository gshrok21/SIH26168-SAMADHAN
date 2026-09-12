package com.navdr.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navdr.ui.components.PrimaryButton
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.DeepNavy
import com.navdr.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch

data class OnboardingPageData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val badgeText: String
)

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit
) {
    val pages = listOf(
        OnboardingPageData(
            title = "Navigate Without Losing the Route",
            description = "Continue navigating smoothly even when GPS or GNSS satellite signals become unreliable or completely unavailable.",
            icon = Icons.Default.GpsFixed,
            badgeText = "GNSS RESILIENCE"
        ),
        OnboardingPageData(
            title = "Smart Sensor-Based Positioning",
            description = "Leverage 9-axis motion sensors, accelerometers, gyroscopes, and intelligent dead-reckoning models to track precise movement.",
            icon = Icons.Default.Sensors,
            badgeText = "SENSOR FUSION ENGINE"
        ),
        OnboardingPageData(
            title = "Seamless Navigation Transition",
            description = "Automatically transition between satellite GNSS positioning and intelligent dead reckoning without stopping your guidance.",
            icon = Icons.Default.CompassCalibration,
            badgeText = "AI DEAD RECKONING"
        )
    )

    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy)
            .padding(24.dp)
    ) {
        // Top skip button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (pagerState.currentPage < pages.size - 1) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AccentCyan,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .clickable { onFinishOnboarding() }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pager Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            val item = pages[pageIndex]
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Card(
                    modifier = Modifier
                        .size(200.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = AccentCyan,
                            modifier = Modifier.size(96.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = item.badgeText,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = AccentCyan,
                    letterSpacing = 1.5.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = item.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Bottom Page Indicator & CTA Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pages.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (isSelected) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) AccentCyan else Color.Gray.copy(alpha = 0.4f))
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        PrimaryButton(
            text = if (pagerState.currentPage == pages.size - 1) "Get Started" else "Next",
            onClick = {
                if (pagerState.currentPage < pages.size - 1) {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onFinishOnboarding()
                }
            }
        )
    }
}
