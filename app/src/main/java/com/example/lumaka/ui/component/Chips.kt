package com.example.lumaka.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ChipColors
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lumaka.R

@Composable
fun CategoryChip(modifier: Modifier = Modifier, selected: Boolean, @StringRes title: Int) =
    FilterChip(
        modifier = modifier,
        selected = selected,
        label = {
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.labelLarge
            )
        },
        shape = RoundedCornerShape(16.dp),

        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.12f),
            disabledLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.36f)
        ),
        elevation = FilterChipDefaults.filterChipElevation(),
        //border = FilterChipDefaults.filterChipBorder(),
        onClick = {},
    )


@Preview
@Composable
private fun CategoryChipPreviewSelected() {
    CategoryChip(selected = true, title = R.string.category_general)
}


@Preview
@Composable
private fun CategoryChipPreviewSelectedLight() {
    CategoryChip(selected = true, title = R.string.category_general)
}

@Preview
@Composable
private fun CategoryChipPreviewNotSelected() {
    CategoryChip(selected = false, title = R.string.category_general)
}

@Preview
@Composable
private fun CategoryChipPreviewNotSelectedDark() {
    CategoryChip(selected = false, title = R.string.category_general)
}