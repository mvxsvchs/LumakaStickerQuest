package com.example.lumaka.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.lumaka.ui.navigation.AppScreens


sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
) {
    object Home : NavigationItem(AppScreens.HOME, Icons.Filled.Home)
    object Profile : NavigationItem(AppScreens.PROFILE, Icons.Filled.Person)
    object Settings : NavigationItem(AppScreens.SETTINGS, Icons.Filled.Settings)
}

val items = listOf(
    NavigationItem.Home,
    NavigationItem.Profile,
    NavigationItem.Settings
)

@Composable
fun NavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { 
                    Icon(
                        imageVector = item.icon,
                        contentDescription = "",
                        modifier = Modifier.size(36.dp)
                    ) 
                },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
            )
        }
    }
}
