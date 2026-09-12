package com.navdr.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navdr.model.GnssStatus
import com.navdr.model.PositionMode
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.DeepNavy
import com.navdr.ui.theme.PrimaryBlue
import com.navdr.ui.theme.StatusDangerRed
import com.navdr.ui.theme.StatusSuccessEmerald
import com.navdr.ui.theme.StatusWarningAmber

@Composable
fun MockMapCanvas(
    gnssStatus: GnssStatus,
    positionMode: PositionMode,
    confidence: Int,
    isNavigating: Boolean = true,
    destinationName: String = "BHU Main Gate",
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    // Pulsing pulse animation for accuracy ring
    val infiniteTransition = rememberInfiniteTransition(label = "PulseTransition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    val progressAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ProgressAnim"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. Draw Map Base Background (Dark Navy)
        drawRect(color = DeepNavy)

        // 2. Draw Urban Block Structures & Buildings
        val buildingColor = Color(0xFF131C2E)
        drawRoundRect(
            color = buildingColor,
            topLeft = Offset(width * 0.05f, height * 0.1f),
            size = Size(width * 0.35f, height * 0.18f),
            cornerRadius = CornerRadius(8.dp.toPx())
        )
        drawRoundRect(
            color = buildingColor,
            topLeft = Offset(width * 0.55f, height * 0.08f),
            size = Size(width * 0.4f, height * 0.22f),
            cornerRadius = CornerRadius(8.dp.toPx())
        )
        drawRoundRect(
            color = buildingColor,
            topLeft = Offset(width * 0.08f, height * 0.55f),
            size = Size(width * 0.38f, height * 0.25f),
            cornerRadius = CornerRadius(8.dp.toPx())
        )
        drawRoundRect(
            color = buildingColor,
            topLeft = Offset(width * 0.58f, height * 0.58f),
            size = Size(width * 0.35f, height * 0.22f),
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // 3. Draw Road Grid Lines
        val roadColor = Color(0xFF1E293B)
        val mainRoadColor = Color(0xFF334155)

        // Secondary cross roads
        drawLine(roadColor, Offset(0f, height * 0.32f), Offset(width, height * 0.32f), strokeWidth = 24.dp.toPx())
        drawLine(roadColor, Offset(width * 0.48f, 0f), Offset(width * 0.48f, height), strokeWidth = 28.dp.toPx())
        drawLine(roadColor, Offset(0f, height * 0.82f), Offset(width, height * 0.82f), strokeWidth = 20.dp.toPx())

        // Road Names
        drawText(
            textMeasurer = textMeasurer,
            text = "Lanka Road",
            style = TextStyle(color = Color(0xFF64748B), fontSize = 11.sp),
            topLeft = Offset(width * 0.12f, height * 0.30f)
        )
        drawText(
            textMeasurer = textMeasurer,
            text = "MG Road Corridor",
            style = TextStyle(color = Color(0xFF64748B), fontSize = 11.sp),
            topLeft = Offset(width * 0.51f, height * 0.42f)
        )

        // 4. Define Waypoints for Route
        val pStart = Offset(width * 0.25f, height * 0.85f)
        val pWay1 = Offset(width * 0.25f, height * 0.32f)
        val pWay2 = Offset(width * 0.75f, height * 0.32f)
        val pEnd = Offset(width * 0.75f, height * 0.15f)

        // 5. Draw Route Line (Solid Blue vs Dashed Cyan/Amber for Dead Reckoning!)
        val solidRoutePath = Path().apply {
            moveTo(pStart.x, pStart.y)
            lineTo(pWay1.x, pWay1.y)
            lineTo(pWay2.x, pWay2.y)
        }

        val deadReckoningPath = Path().apply {
            moveTo(pWay2.x, pWay2.y)
            lineTo(pEnd.x, pEnd.y)
        }

        val isDeadReckoningActive = positionMode == PositionMode.DEAD_RECKONING || gnssStatus == GnssStatus.GNSS_LOST

        // Draw First Segment (Solid Blue - GNSS Confirmed Route)
        drawPath(
            path = solidRoutePath,
            color = PrimaryBlue,
            style = Stroke(
                width = 10.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // Draw Second Segment (Solid Blue if GNSS, or Dashed Cyan/Amber if Dead Reckoning Active)
        if (isDeadReckoningActive) {
            val strokeColor = if (confidence >= 75) AccentCyan else StatusWarningAmber
            drawPath(
                path = deadReckoningPath,
                color = strokeColor,
                style = Stroke(
                    width = 10.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 15f), 0f)
                )
            )

            // Draw DR Banner Marker on Path where transition occurred
            drawCircle(
                color = StatusWarningAmber,
                radius = 8.dp.toPx(),
                center = pWay2
            )
            drawCircle(
                color = Color.White,
                radius = 4.dp.toPx(),
                center = pWay2
            )
        } else {
            drawPath(
                path = deadReckoningPath,
                color = PrimaryBlue,
                style = Stroke(
                    width = 10.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // 6. Draw Destination Flag Marker Pin
        drawCircle(color = StatusDangerRed.copy(alpha = 0.3f), radius = 18.dp.toPx(), center = pEnd)
        drawCircle(color = StatusDangerRed, radius = 9.dp.toPx(), center = pEnd)
        drawCircle(color = Color.White, radius = 4.dp.toPx(), center = pEnd)

        drawText(
            textMeasurer = textMeasurer,
            text = "★ $destinationName",
            style = TextStyle(color = Color.White, fontSize = 12.sp),
            topLeft = Offset(pEnd.x - 45.dp.toPx(), pEnd.y - 30.dp.toPx())
        )

        // 7. Compute Moving Current Vehicle Position Along Route
        val currPos = if (!isNavigating) {
            Offset(width * 0.45f, height * 0.32f)
        } else {
            val t = progressAnim
            when {
                t < 0.4f -> {
                    val ratio = t / 0.4f
                    Offset(pStart.x, pStart.y + (pWay1.y - pStart.y) * ratio)
                }
                t < 0.8f -> {
                    val ratio = (t - 0.4f) / 0.4f
                    Offset(pWay1.x + (pWay2.x - pWay1.x) * ratio, pWay1.y)
                }
                else -> {
                    val ratio = (t - 0.8f) / 0.2f
                    Offset(pWay2.x + (pEnd.x - pWay2.x) * ratio, pWay2.y + (pEnd.y - pWay2.y) * ratio)
                }
            }
        }

        // 8. Draw Position Marker Accuracy Circle based on Confidence (HIGH, MEDIUM, LOW)
        val (accuracyRingColor, ringBaseRadius) = when {
            confidence >= 90 -> Pair(AccentCyan, 28.dp.toPx())
            confidence >= 75 -> Pair(StatusWarningAmber, 40.dp.toPx())
            else -> Pair(StatusDangerRed.copy(alpha = 0.6f), 55.dp.toPx())
        }

        // Translucent Outer Accuracy Ring (Pulsing)
        drawCircle(
            color = accuracyRingColor.copy(alpha = 0.25f),
            radius = ringBaseRadius * pulseScale,
            center = currPos
        )

        // Accuracy Ring Outline
        drawCircle(
            color = accuracyRingColor,
            radius = ringBaseRadius,
            center = currPos,
            style = Stroke(width = 2.dp.toPx())
        )

        // Solid Vehicle Center Point Marker (Blue/Cyan Pin)
        val dotColor = if (isDeadReckoningActive) AccentCyan else PrimaryBlue
        drawCircle(color = dotColor, radius = 10.dp.toPx(), center = currPos)
        drawCircle(color = Color.White, radius = 5.dp.toPx(), center = currPos)
    }
}
