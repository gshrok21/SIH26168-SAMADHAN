package com.navdr.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.navdr.navigation.Routes
import com.navdr.ui.theme.AccentCyan
import com.navdr.ui.theme.DarkCardBg
import com.navdr.ui.theme.PrimaryBlue
import com.navdr.ui.theme.SecondaryTextDarkTheme

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    object Home : BottomNavItem(Routes.HOME, "Home", Icons.Default.Home)
    object Navigate : BottomNavItem(Routes.SEARCH, "Navigate", Icons.Default.Navigation)
    object History : BottomNavItem(Routes.HISTORY, "History", Icons.Default.History)
    object System : BottomNavItem(Routes.SYSTEM, "System", Icons.Default.Memory)
}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Navigate,
        BottomNavItem.History,
        BottomNavItem.System
    )

    NavigationBar(
        containerColor = DarkCardBg,
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AccentCyan,
                    selectedTextColor = AccentCyan,
                    indicatorColor = PrimaryBlue.copy(alpha = 0.25f),
                    unselectedIconColor = SecondaryTextDarkTheme,
                    unselectedTextColor = SecondaryTextDarkTheme
                )
            )
        }
    }
}
