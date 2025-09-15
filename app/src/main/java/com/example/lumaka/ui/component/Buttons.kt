package com.example.lumaka.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lumaka.ui.theme.LumakaTheme
import androidx.compose.foundation.isSystemInDarkTheme

@Composable
fun NavigationButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
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
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun NavigationButtonPreviewLightEnabled() {
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        NavigationButton(modifier = Modifier.padding(all = 16.dp), text = "Button", onClick = {})
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun NavigationButtonPreviewLightDisabled() {
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        NavigationButton(modifier = Modifier.padding(all = 16.dp), text = "Button", onClick = {}, enabled = false)
    }
}


@Preview(name = "Dark", showBackground = true)
@Composable
private fun NavigationButtonPreviewDarkEnabled() {
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        NavigationButton(modifier = Modifier.padding(all = 16.dp), text = "Button", onClick = {})
    }
}

@Preview(name = "Dark", showBackground = true)
@Composable
private fun NavigationButtonPreviewDarkDisabled() {
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        NavigationButton(modifier = Modifier.padding(all = 16.dp), text = "Button", onClick = {}, enabled = false)
    }
}