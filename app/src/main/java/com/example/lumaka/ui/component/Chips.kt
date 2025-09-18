package com.example.lumaka.ui.component

import android.graphics.drawable.Drawable
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import com.example.lumaka.R
import com.example.lumaka.ui.theme.LumakaTheme

@Composable
fun CategoryChip(
    modifier: Modifier = Modifier,
    selected: Boolean,
    @StringRes title: Int,
    onClick: () -> Unit
) =
    FilterChip(
        modifier = modifier,
        selected = selected,
        label = {
            Text(
                text = stringResource(id = title),
                style = MaterialTheme.typography.labelMedium,
                fontSize = 14.sp,
            )
        },
        shape = RoundedCornerShape(size = 16.dp),
        leadingIcon = {
            if (selected) {
                Icon(
                    modifier = Modifier.size(size = 16.dp),
                    painter = painterResource(
                        id = R.drawable.chip_check_24
                    ),
                    contentDescription = null,

                    )
            }
        },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface,
        ),
        elevation = FilterChipDefaults.filterChipElevation(),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.primary,
            selectedBorderColor = MaterialTheme.colorScheme.onSurface,
            borderWidth = 1.dp,
            selectedBorderWidth = 2.dp
        ),
        onClick = { onClick() },
    )


@Preview
@Composable
private fun CategoryChipPreviewSelected() {
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        CategoryChip(selected = true, title = R.string.category_general, onClick = {})
    }
}


@Preview
@Composable
private fun CategoryChipPreviewSelectedLight() {
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        CategoryChip(selected = true, title = R.string.category_general, onClick = {})
    }
}

@Preview
@Composable
private fun CategoryChipPreviewNotSelected() {
    LumakaTheme(darkTheme = false, dynamicColor = false) {
        CategoryChip(selected = false, title = R.string.category_general, onClick = {})
    }
}

@Preview
@Composable
private fun CategoryChipPreviewNotSelectedDark() {
    LumakaTheme(darkTheme = true, dynamicColor = false) {
        CategoryChip(selected = false, title = R.string.category_general, onClick = {})
    }
}