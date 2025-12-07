package com.example.lumaka.ui.feature.home

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lumaka.data.remote.api.QuestService
import com.example.lumaka.data.remote.dto.LoginDTO
import com.example.lumaka.data.remote.dto.PointsDTO
import com.example.lumaka.data.remote.dto.RegisterDTO
import com.example.lumaka.data.remote.dto.UserDTO
import kotlinx.coroutines.launch
import kotlin.math.abs
import com.example.lumaka.R
import com.example.lumaka.domain.model.CategoryEnum
import com.example.lumaka.domain.model.Task
import com.example.lumaka.ui.component.CategoryChip
import com.example.lumaka.ui.component.NavigationBar
import com.example.lumaka.ui.component.SelectionDropdownMenu
import com.example.lumaka.ui.component.TextInputField
import com.example.lumaka.ui.component.TopBarText
import com.example.lumaka.ui.theme.LumakaTheme
import com.example.lumaka.util.rememberPreviewNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val tasks by homeViewModel.tasks.collectAsState()
    val selectedChipId = remember { mutableIntStateOf(value = 0) }
    val selectedDropdownId = remember { mutableIntStateOf(value = 0) }
    val textInputState = remember { mutableStateOf(value = "") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            Column {
                TopBarText()
                ChipsSelection(selectedChipId = selectedChipId)
                InputRow(
                    textInputState = textInputState,
                    selectedDropdownId = selectedDropdownId,
                    homeViewModel = homeViewModel
                )
            }
        },
        bottomBar = {
            NavigationBar(navController = navController)
        }
    ) { padding ->
        TaskList(
            padding = padding,
            tasks = tasks,
            selectedCategoryId = selectedChipId.intValue,
            onToggle = { id -> homeViewModel.toggleTask(id) },
            onDelete = { id -> homeViewModel.removeTask(id) },
            onMove = { id, delta -> homeViewModel.moveTask(id, delta) }
        )
    }
}


@Composable
private fun ChipsSelection(selectedChipId: MutableIntState) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        val categories = CategoryEnum.entries.toTypedArray()
        items(items = categories) { category ->
            val isSelected =
                remember(key1 = selectedChipId.intValue) { derivedStateOf { selectedChipId.intValue == category.id } }
            CategoryChip(
                modifier = Modifier
                    .height(height = 40.dp),
                selected = isSelected.value,
                title = category.title,
                onClick = { selectedChipId.intValue = category.id })
        }
    }
}

@Composable
private fun InputRow(
    textInputState: MutableState<String>,
    selectedDropdownId: MutableIntState,
    homeViewModel: HomeViewModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DropdownSelection(selectedDropdownId = selectedDropdownId)

        TextInputField(
            modifier = Modifier
                .height(height = 64.dp)
                .weight(weight = 1f),
            currentText = textInputState.value,
            onTextChange = { textInputState.value = it },
            placeholder = R.string.home_new_tasks,

        )

        IconButton(
            onClick = {
                val category = CategoryEnum.entries.find { it.id == selectedDropdownId.intValue } ?: CategoryEnum.ALL
                homeViewModel.addTask(title = textInputState.value, category = category)
                textInputState.value = ""
            }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(id = R.string.home_add_task)
            )
        }
    }
}

@Composable
private fun DropdownSelection(selectedDropdownId: MutableIntState) {
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
                    width = 124.dp
                )
                .height(
                    height = 64.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                fontSize = 14.sp,
                text = stringResource(
                    id = CategoryEnum.entries.find { it.id == selectedDropdownId.intValue }?.title
                        ?: R.string.category_all
                )
            )
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(id = R.string.home_dropdown_icon)
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
}

@Composable
private fun TaskList(
    padding: PaddingValues,
    tasks: List<Task>,
    selectedCategoryId: Int,
    onToggle: (Int) -> Unit,
    onDelete: (Int) -> Unit,
    onMove: (Int, Int) -> Unit
) {
    val visibleTasks = remember(tasks, selectedCategoryId) {
        tasks.filter { selectedCategoryId == 0 || it.category.id == selectedCategoryId }
    }

    if (visibleTasks.isEmpty()) {
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
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = visibleTasks, key = { it.id }) { task ->
            val scope = rememberCoroutineScope()
            val offsetX = remember { Animatable(0f) }
            val offsetY = remember { Animatable(0f) }
            val dragY = remember { mutableStateOf(0f) }
            val alpha by animateFloatAsState(
                targetValue = if (abs(offsetX.value) > 20f) 0.7f else 1f,
                animationSpec = tween(durationMillis = 150),
                label = "swipe alpha"
            )
            var dragMode by remember { mutableStateOf<DragMode?>(null) }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationX = offsetX.value
                        translationY = offsetY.value
                        this.alpha = alpha
                    }
                    .pointerInput(task.id) {
                        detectDragGestures(
                            onDragStart = {
                                dragMode = null
                                dragY.value = 0f
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                if (dragMode == null) {
                                    dragMode = if (abs(dragAmount.x) > abs(dragAmount.y)) DragMode.Horizontal else DragMode.Vertical
                                }
                                when (dragMode) {
                                    DragMode.Horizontal -> {
                                        scope.launch {
                                            offsetX.snapTo(offsetX.value + dragAmount.x)
                                        }
                                    }
                                    DragMode.Vertical -> {
                                        dragY.value += dragAmount.y
                                        scope.launch {
                                            offsetY.snapTo(offsetY.value + dragAmount.y)
                                        }
                                    }
                                    null -> Unit
                                }
                            },
                            onDragEnd = {
                                when (dragMode) {
                                    DragMode.Horizontal -> {
                                        val current = offsetX.value
                                        if (current < -80f) {
                                            val target = -800f
                                            scope.launch {
                                                offsetX.animateTo(target, tween(200))
                                                onDelete(task.id)
                                                offsetX.snapTo(0f)
                                            }
                                        } else {
                                            // Snap back for right swipes or small drags
                                            scope.launch { offsetX.animateTo(0f, tween(180)) }
                                        }
                                    }
                                    DragMode.Vertical -> {
                                        when {
                                            dragY.value > 40f -> onMove(task.id, 1)
                                            dragY.value < -40f -> onMove(task.id, -1)
                                        }
                                        dragY.value = 0f
                                        scope.launch {
                                            offsetY.animateTo(
                                                targetValue = 0f,
                                                animationSpec = spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                                )
                                            )
                                        }
                                    }
                                    null -> Unit
                                }
                                dragMode = null
                            },
                            onDragCancel = {
                                dragMode = null
                                dragY.value = 0f
                                scope.launch {
                                    offsetX.animateTo(0f, tween(180))
                                    offsetY.animateTo(
                                        targetValue = 0f,
                                        animationSpec = spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                        )
                                    )
                                }
                            }
                        )
                    },
                tonalElevation = 2.dp,
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(id = task.category.title),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Checkbox(
                        checked = task.completed,
                        onCheckedChange = { onToggle(task.id) }
                    )
                }
            }
        }
    }
}

private enum class DragMode { Horizontal, Vertical }


@Preview(name = "Home Light", showBackground = true)
@Composable
private fun HomeScreenPreviewLight() {
    val previewNavController = rememberPreviewNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val previewVm = remember {
        val dummyApi = object : QuestService {
            override suspend fun registerUser(register: RegisterDTO) {}
            override suspend fun loginUser(login: LoginDTO): UserDTO? = null
            override suspend fun getUserById(userid: Int): UserDTO? = null
            override suspend fun updatePoints(pointsDTO: PointsDTO) {}
            override suspend fun createTask(request: com.example.lumaka.data.remote.dto.TaskCreateRequest): com.example.lumaka.data.remote.dto.TaskCreateResponse =
                com.example.lumaka.data.remote.dto.TaskCreateResponse(taskId = 1)
            override suspend fun getTasks(userId: Int): List<com.example.lumaka.data.remote.dto.TaskResponse> = emptyList()
            override suspend fun deleteTask(taskId: Int) {}
            override suspend fun updateTaskCompletion(
                taskId: Int,
                request: com.example.lumaka.data.remote.dto.TaskUpdateRequest
            ): com.example.lumaka.data.remote.dto.TaskUpdateResponse =
                com.example.lumaka.data.remote.dto.TaskUpdateResponse(userPoints = 0)
            override suspend fun updateStickers(
                userId: Int,
                request: com.example.lumaka.data.remote.dto.UpdateStickersRequest
            ) { }
        }
        HomeViewModel(
            pointsRepository = com.example.lumaka.data.repository.PointsRepository(context, dummyApi),
            sessionRepository = com.example.lumaka.data.repository.SessionRepository(context),
            taskRepository = com.example.lumaka.data.repository.TaskRepository(dummyApi)
        )
    }
    LumakaTheme {
        HomeView(previewNavController, homeViewModel = previewVm)
    }
}

@Preview(name = "Home Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenPreviewDark() {
    val previewNavController = rememberPreviewNavController()
    val context = androidx.compose.ui.platform.LocalContext.current
    val previewVm = remember {
        val dummyApi = object : QuestService {
            override suspend fun registerUser(register: RegisterDTO) {}
            override suspend fun loginUser(login: LoginDTO): UserDTO? = null
            override suspend fun getUserById(userid: Int): UserDTO? = null
            override suspend fun updatePoints(pointsDTO: PointsDTO) {}
            override suspend fun createTask(request: com.example.lumaka.data.remote.dto.TaskCreateRequest): com.example.lumaka.data.remote.dto.TaskCreateResponse =
                com.example.lumaka.data.remote.dto.TaskCreateResponse(taskId = 1)
            override suspend fun getTasks(userId: Int): List<com.example.lumaka.data.remote.dto.TaskResponse> = emptyList()
            override suspend fun deleteTask(taskId: Int) {}
            override suspend fun updateTaskCompletion(
                taskId: Int,
                request: com.example.lumaka.data.remote.dto.TaskUpdateRequest
            ): com.example.lumaka.data.remote.dto.TaskUpdateResponse =
                com.example.lumaka.data.remote.dto.TaskUpdateResponse(userPoints = 0)
            override suspend fun updateStickers(
                userId: Int,
                request: com.example.lumaka.data.remote.dto.UpdateStickersRequest
            ) { }
        }
        HomeViewModel(
            pointsRepository = com.example.lumaka.data.repository.PointsRepository(context, dummyApi),
            sessionRepository = com.example.lumaka.data.repository.SessionRepository(context),
            taskRepository = com.example.lumaka.data.repository.TaskRepository(dummyApi)
        )
    }
    LumakaTheme {
        HomeView(previewNavController, homeViewModel = previewVm)
    }
}
