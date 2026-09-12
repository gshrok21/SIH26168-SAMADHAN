package com.navdr.model

import androidx.compose.ui.graphics.Color

enum class GnssStatus(
    val label: String,
    val displayIconText: String,
    val hexColor: String
) {
    GNSS_AVAILABLE("GNSS Strong", "● Strong", "#10B981"),
    GNSS_WEAK("GNSS Weak", "⚠ Weak", "#F59E0B"),
    GNSS_LOST("GNSS Lost", "× Lost", "#EF4444"),
    GNSS_RECOVERED("GNSS Recovered", "✓ Recovered", "#10B981")
}
