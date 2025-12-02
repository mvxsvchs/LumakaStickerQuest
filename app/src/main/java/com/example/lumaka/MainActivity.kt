package com.example.lumaka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lumaka.ui.feature.home.HomeView
import com.example.lumaka.ui.feature.login.Login
import com.example.lumaka.ui.feature.login.StartView
import com.example.lumaka.ui.feature.profile.ProfileRoute
import com.example.lumaka.ui.feature.settings.SettingsView
import com.example.lumaka.ui.feature.settings.PrivacyView
import com.example.lumaka.ui.feature.register.Register
import com.example.lumaka.ui.navigation.AppScreens
import com.example.lumaka.ui.theme.LumakaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LumakaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.START) {
        composable(route = AppScreens.START) {
            StartView(
                navController = navController,
            )
        }
        composable(route = AppScreens.HOME) {
            HomeView(
                navController = navController,
            )
        }
        composable(route = AppScreens.PROFILE) {
            ProfileRoute(
                navController = navController
            )
        }
        composable(route = AppScreens.SETTINGS) {
            SettingsView(
                navController = navController
            )
        }
        composable(route = AppScreens.PRIVACY) {
            PrivacyView(
                navController = navController
            )
        }
        composable(route = AppScreens.LOGIN) {
            Login(
                navController = navController,
            )
        }
        composable(route = AppScreens.REGISTER) {
            Register(
                navController = navController,
            )
        }
    }
}
