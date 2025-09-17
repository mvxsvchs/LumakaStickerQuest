package com.example.lumaka.ui.component

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lumaka.domain.model.CategoryEnum


@Composable
fun SelectionDropdownMenu(
    modifier: Modifier,
    expanded: Boolean,
    selectedCategoryId: Int,
    onDismiss: () -> Unit = {},
    onItemClick: (categoryId: Int) -> Unit = { },
) = DropdownMenu(
    modifier = modifier,
    expanded = expanded,
    onDismissRequest = { onDismiss() },
    containerColor = MaterialTheme.colorScheme.surface,
) {
    val categories = CategoryEnum.entries.toTypedArray()
    categories.forEach { category ->
        val isSelected =
            remember(key1 = selectedCategoryId) { derivedStateOf { selectedCategoryId == category.id } }
        DropdownMenuItem(
            text = { Text(text = stringResource(id = category.title)) },
            onClick = { onItemClick(category.id) },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.onSurface,
            )
        )
    }
}


@Preview
@Composable
private fun SelectionDropdownMenuPreview() {
    SelectionDropdownMenu(modifier = Modifier, expanded = true, selectedCategoryId = 0,)

}