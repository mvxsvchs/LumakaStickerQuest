package com.example.lumaka.ui.feature.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lumaka.R
import com.example.lumaka.ui.component.NavigationBar
import com.example.lumaka.ui.component.TopBarText
import com.example.lumaka.ui.theme.LumakaTheme
import com.example.lumaka.util.rememberPreviewNavController
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.LaunchedEffect
import com.example.lumaka.ui.navigation.AppScreens

@Composable
fun SettingsView(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val logoutRequested = settingsViewModel.logoutRequested.collectAsState()

    LaunchedEffect(logoutRequested.value) {
        if (logoutRequested.value) {
            navController.navigate(route = AppScreens.START) {
                popUpTo(AppScreens.START) { inclusive = true }
            }
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBarText() },
        bottomBar = { NavigationBar(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = stringResource(id = R.string.settings_title),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Divider()
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.settings_notifications),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Divider()
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.settings_privacy),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Button(
                onClick = { settingsViewModel.logout() }
            ) {
                Text(text = stringResource(id = R.string.settings_logout))
            }
        }
    }
}

@Preview(name = "Settings Light", showBackground = true)
@Composable
private fun SettingsPreviewLight() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        SettingsView(previewNavController)
    }
}

@Preview(name = "Settings Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SettingsPreviewDark() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        SettingsView(previewNavController)
    }
}
