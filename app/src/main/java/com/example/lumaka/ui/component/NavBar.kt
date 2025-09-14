package com.example.lumaka.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


sealed class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Home : NavigationItem("home", Icons.Filled.Home, "Home")
    object Profile : NavigationItem("profile", Icons.Filled.Person, "Profile")
    object Settings : NavigationItem("settings", Icons.Filled.Settings, "Settings")
}

val items = listOf(
    NavigationItem.Home,
    NavigationItem.Profile,
    NavigationItem.Settings
)

@Composable
fun NavigationBar(navController: NavController) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(imageVector = item.icon, contentDescription = item.title) },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
            )
        }
    }
}