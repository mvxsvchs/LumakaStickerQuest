package com.example.lumaka.ui.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lumaka.ui.component.NavigationButton
import com.example.lumaka.ui.theme.*
import com.example.lumaka.util.rememberPreviewNavController
import com.example.lumaka.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.topbar_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
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

            Text(
                text = stringResource(id = R.string.home_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Navigation Button
            NavigationButton(
                text = stringResource(id = R.string.button_home_title),
                onClick = { /* navController.navigate(...) */ }
            )
        }
    }
}

@Preview(name = "Home Light", showBackground = true)
@Composable
private fun HomeScreenPreviewLight() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        HomeScreen(previewNavController)
    }
}

@Preview(name = "Home Dark", showBackground = true)
@Composable
private fun HomeScreenPreviewDark() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        HomeScreen(previewNavController)
    }
}
