package com.example.lumaka.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lumaka.R
import com.example.lumaka.ui.theme.LumakaTheme

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    @StringRes
    textId: Int,
) {
    val container = MaterialTheme.colorScheme.primary
    val content = MaterialTheme.colorScheme.onPrimary

    Button(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.24f),
            disabledContentColor = content.copy(alpha = 0.36f)
        ),
        onClick = onClick,
    ) {
        Text(
            text = stringResource(textId),
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun IconButtonPreviewLightEnabled() {
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        TextButton(
            modifier = Modifier.padding(all = 16.dp),
            textId = R.string.login_login,
            onClick = {})
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun IconButtonPreviewLightDisabled() {
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        TextButton(
            modifier = Modifier.padding(all = 16.dp),
            textId = R.string.login_login,
            onClick = {},
            enabled = false
        )
    }
}


@Preview(name = "Dark", showBackground = true)
@Composable
private fun IconButtonPreviewDarkEnabled() {
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        TextButton(
            modifier = Modifier.padding(all = 16.dp),
            textId = R.string.login_register,
            onClick = {})
    }
}

@Preview(name = "Dark", showBackground = true)
@Composable
private fun IconButtonPreviewDarkDisabled() {
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        TextButton(
            modifier = Modifier.padding(all = 16.dp),
            textId = R.string.login_register,
            onClick = {},
            enabled = false
        )
    }
}