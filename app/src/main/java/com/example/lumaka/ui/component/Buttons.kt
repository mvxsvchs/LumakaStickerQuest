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
    text: String = "Los geht’s",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    // Light: Primary / OnPrimary (wie vorher)
    // Dark:  Secondary / OnSecondary (besserer Kontrast)
    val container = if (isDark) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
    val content   = if (isDark) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary

    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = container,
            contentColor = content,
            disabledContainerColor = container.copy(alpha = 0.12f),
            disabledContentColor = content.copy(alpha = 0.38f)
        ),
        modifier = modifier
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(name = "Light", showBackground = true)
@Composable
private fun NavigationButtonPreviewLight() {
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        NavigationButton(onClick = {}, modifier = Modifier.padding(16.dp))
    }
}

@Preview(name = "Dark", showBackground = true)
@Composable
private fun NavigationButtonPreviewDark() {
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        NavigationButton(onClick = {}, modifier = Modifier.padding(16.dp))
    }
}
