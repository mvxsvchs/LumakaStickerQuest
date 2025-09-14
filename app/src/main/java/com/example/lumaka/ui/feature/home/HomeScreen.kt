package com.example.lumaka.ui.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lumaka.ui.component.NavigationButton
import com.example.lumaka.ui.theme.*
import com.example.lumaka.util.rememberPreviewNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isDark: Boolean,
    onToggleDark: () -> Unit
) {
    val backgroundColor = if (isDark) DarkBg else PeachWhite

    Scaffold(
        containerColor = backgroundColor,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = { Text("Lumaka") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) DarkSurface else SoftPink,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(onClick = onToggleDark) {
                        AnimatedContent(
                            targetState = isDark,
                            label = "themeToggle",
                            transitionSpec = { fadeIn() togetherWith fadeOut() }
                        ) { dark ->
                            if (dark) {
                                Icon(Icons.Outlined.LightMode, contentDescription = "Light Mode")
                            } else {
                                Icon(Icons.Outlined.DarkMode, contentDescription = "Dark Mode")
                            }
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Willkommen-Text direkt
            Text(
                text = "Willkommen bei Lumaka!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Navigation Button
            NavigationButton(
                text = "Los geht’s",
                onClick = { /* navController.navigate(...) */ }
            )
        }
    }
}

@Preview(name = "Home Light Solid", showBackground = true)
@Composable
private fun HomeScreenPreviewLight() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        HomeScreen(previewNavController, isDark = false, onToggleDark = {})
    }
}

@Preview(name = "Home Dark Solid", showBackground = true)
@Composable
private fun HomeScreenPreviewDark() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        HomeScreen(previewNavController, isDark = true, onToggleDark = {})
    }
}
