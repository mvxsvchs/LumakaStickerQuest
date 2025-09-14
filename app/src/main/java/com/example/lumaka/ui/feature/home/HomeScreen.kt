package com.example.lumaka.ui.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.lumaka.ui.component.NavigationButton
import com.example.lumaka.ui.theme.LumakaTheme
import com.example.lumaka.util.rememberPreviewNavController

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Text(text = "Hallo Jantjes",style = MaterialTheme.typography.bodyLarge)
            NavigationButton {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        HomeScreen(navController = previewNavController)
    }
}