package com.example.lumaka.ui.feature.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lumaka.R
import com.example.lumaka.ui.component.TextButton
import com.example.lumaka.ui.navigation.AppScreens
import com.example.lumaka.ui.theme.LumakaTheme
import com.example.lumaka.util.rememberPreviewNavController


@Composable
fun StartView(
    navController: NavController
) {
    Scaffold(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 48.dp, end = 48.dp),
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                TextButton(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth(),
                    textId = R.string.login_login,
                    onClick = { navController.navigate(route = AppScreens.LOGIN) }
                )
                TextButton(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth(),
                    textId = R.string.login_register,
                    onClick = { navController.navigate(route = AppScreens.REGISTER) }
                )
            }

        }

    }
}


@Preview(name = "StartView Light", showBackground = true)
@Composable
private fun StartViewPreviewLight() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        StartView(previewNavController)
    }
}

@Preview(name = "StartView Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun StartViewPreviewDark() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        StartView(previewNavController)
    }
}