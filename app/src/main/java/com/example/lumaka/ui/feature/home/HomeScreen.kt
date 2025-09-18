package com.example.lumaka.ui.feature.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.lumaka.ui.component.IconButton
import com.example.lumaka.ui.theme.*
import com.example.lumaka.util.rememberPreviewNavController
import com.example.lumaka.R
import com.example.lumaka.domain.model.CategoryEnum
import com.example.lumaka.ui.component.CategoryChip
import com.example.lumaka.ui.component.NavigationBar
import com.example.lumaka.ui.component.SelectionDropdownMenu
import com.example.lumaka.ui.component.TextInputField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController
) {

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            val selectedChipId = remember { mutableIntStateOf(value = 0) }
            val selectedDropdownId = remember { mutableIntStateOf(value = 0) }
            val textInputState = remember { mutableStateOf(value = "") }

            Column {
                TopAppBar(
                    title = {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = stringResource(id = R.string.topbar_title),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onBackground,
                        actionIconContentColor = MaterialTheme.colorScheme.onBackground
                    ),
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    val categories = CategoryEnum.entries.toTypedArray()
                    items(items = categories) { category ->
                        val isSelected =
                            remember(key1 = selectedChipId.intValue) { derivedStateOf { selectedChipId.intValue == category.id } }
                        CategoryChip(
                            selected = isSelected.value,
                            title = category.title,
                            onClick = { selectedChipId.intValue = category.id })
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    val expanded = remember { mutableStateOf(value = false) }

                    Box {
                        Row(
                            modifier = Modifier
                                .clickable { expanded.value = true }
                                .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(size = 8.dp),
                                )
                                .width(
                                    width = 154.dp
                                )
                                .height(
                                    height = 48.dp
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                style = MaterialTheme.typography.labelMedium,
                                fontSize = 18.sp,
                                text = stringResource(
                                    id = CategoryEnum.entries.find { it.id == selectedDropdownId.intValue }?.title
                                        ?: R.string.category_all
                                )
                            )
                            Icon(
                                modifier = Modifier.padding(end = 8.dp),
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown Icon"
                            )
                        }

                        SelectionDropdownMenu(
                            modifier = Modifier,
                            expanded = expanded.value,
                            selectedCategoryId = selectedDropdownId.intValue,
                            onDismiss = { expanded.value = false },
                            onItemClick = { categoryId ->
                                selectedDropdownId.intValue = categoryId
                                expanded.value = false
                            }
                        )
                    }

                    TextInputField(
                        modifier = Modifier
                            .weight(weight = 1f),
                        currentText = textInputState.value,
                        onTextChange = { textInputState.value = it },
                    )


                    IconButton(
                        modifier = Modifier
                            .height(
                                height = 48.dp
                            ),
                        iconId = R.drawable.button_add_24,
                        onClick = { /* navController.navigate(...) */
                        }
                    )
                }
            }
        },
        bottomBar = {
            NavigationBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = stringResource(id = R.string.home_no_tasks),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview(name = "Home Light", showBackground = true)
@Composable
private fun HomeScreenPreviewLight() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        HomeScreen(previewNavController)
    }
}

@Preview(name = "Home Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreviewDark() {
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        HomeScreen(previewNavController)
    }
}
