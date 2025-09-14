package com.example.lumaka.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lumaka.ui.theme.LumakaTheme

@Composable
fun NavigationButton(onClick: () -> Unit)=
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(size = 12.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        )
    ) {
        Text(text = "Hallo Welt", style = MaterialTheme.typography.labelLarge,)
    }

@Preview
@Composable
fun NavigationButtonPreview(){
    LumakaTheme {
        NavigationButton(onClick = {})
    }
}