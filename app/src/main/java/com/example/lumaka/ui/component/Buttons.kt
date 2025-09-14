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

@Composable
fun NavigationButton(
    text: String = "Hallo Welt",
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
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
