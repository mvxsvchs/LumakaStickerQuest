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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.lumaka.R
import com.example.lumaka.ui.component.TextButton
import com.example.lumaka.ui.component.TextInputField
import com.example.lumaka.ui.component.TopBarText
import com.example.lumaka.ui.navigation.AppScreens
import com.example.lumaka.ui.theme.LumakaTheme
import com.example.lumaka.util.rememberPreviewNavController

@Composable
fun Login(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel(),
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBarText() },
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                TextInputField(
                    modifier = Modifier.fillMaxWidth(),
                    currentText = email,
                    onTextChange = { email = it },
                    placeholder = R.string.login_email
                )
                TextInputField(
                    modifier = Modifier.fillMaxWidth(),
                    currentText = password,
                    onTextChange = {password = it},
                    placeholder = R.string.login_password,
                    shouldHideText = true,
                )
                TextButton(
                    modifier = Modifier
                        .defaultMinSize(minHeight = 48.dp)
                        .fillMaxWidth(),
                    textId = R.string.login_login,
                    onClick = {
                        navController.navigate(route = AppScreens.HOME)
                        loginViewModel.onLogin(
                            email = email,
                            password = password
                        )
                    }
                )
            }
        }
    }
}


@Preview(name = "LoginView Light", showBackground = true)
@Composable
private fun LoginViewPreviewLight() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        Login(previewNavController)
    }
}

@Preview(name = "LoginView Dark",showBackground = true,uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun LoginViewPreviewDark() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        Login(previewNavController)
    }
}

