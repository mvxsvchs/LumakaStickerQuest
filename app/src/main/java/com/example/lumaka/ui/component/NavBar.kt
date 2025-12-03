package com.example.lumaka.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lumaka.ui.navigation.AppScreens
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember


sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
) {
    object Home : NavigationItem(AppScreens.HOME, Icons.Filled.Home)
    object Bingo : NavigationItem(AppScreens.BINGOBOARD, Icons.Filled.GridView)
    object Shop : NavigationItem(AppScreens.SHOP, Icons.Filled.ShoppingCart)
    object Profile : NavigationItem(AppScreens.PROFILE, Icons.Filled.Person)
    object Settings : NavigationItem(AppScreens.SETTINGS, Icons.Filled.Settings)
}

val items = listOf(
    NavigationItem.Home,
    NavigationItem.Profile,
    NavigationItem.Bingo,
    NavigationItem.Shop,
    NavigationItem.Settings
)

@Composable
fun NavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = androidx.compose.material3.NavigationBarDefaults.containerColor
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "",
                        modifier = Modifier.size(36.dp),
                        tint = if (currentRoute == item.route) androidx.compose.material3.MaterialTheme.colorScheme.primary
                        else androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                },
                interactionSource = remember { MutableInteractionSource() },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = androidx.compose.ui.graphics.Color.Transparent,
                    selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                )
            )

        }
    }
}
