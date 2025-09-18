package com.example.lumaka.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.lumaka.R

@Composable
fun TextInputField(
    modifier: Modifier = Modifier,
    currentText: String,
    onTextChange: (String) -> Unit,
) {
    val textState = remember {
        mutableStateOf(
            value = TextFieldValue(
                text = currentText,
                selection = TextRange(index = currentText.length)
            )
        )
    }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current



    TextField(
        modifier = modifier,
        shape = RoundedCornerShape(size = 8.dp),
        value = textState.value,
        singleLine = true,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
        ),
        onValueChange = {
            textState.value = it
            onTextChange(it.text)
        },
        placeholder = { Text(text = stringResource(id = R.string.home_new_tasks)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                focusManager.clearFocus()
                keyboardController?.hide()
            }
        ),

        )
}