package com.example.lumaka.ui.feature.bingo

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lumaka.R
import com.example.lumaka.ui.component.NavigationBar
import com.example.lumaka.ui.component.TopBarText
import com.example.lumaka.ui.theme.LumakaTheme
import com.example.lumaka.util.rememberPreviewNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lumaka.data.session.UserSession
import java.time.Duration
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.temporal.TemporalAdjusters

@Composable
fun BingoBoardRoute(
    navController: NavController,
    viewModel: BingoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val userState by UserSession.user.collectAsState()
    BingoBoardView(
        navController = navController,
        uiState = uiState,
        points = userState?.points ?: 0,
        onBuyRandomField = { viewModel.purchaseRandomCell() },
        onWeekTick = { viewModel.ensureWeekIsCurrent() }
    )
}

@Composable
fun BingoBoardView(
    navController: NavController,
    uiState: BingoUiState,
    points: Int,
    onBuyRandomField: () -> Unit,
    onWeekTick: () -> Unit
) {
    var remainingReset by remember { mutableStateOf("") }

    LaunchedEffect(uiState.weekKey) {
        val zone = ZoneId.of("Europe/Berlin")
        while (true) {
            onWeekTick()
            val now = ZonedDateTime.now(zone)
            val nextReset = nextResetMonday(zone, now)
            val remaining = Duration.between(now, nextReset)
            if (!remaining.isNegative) {
                remainingReset = formatDuration(remaining)
            }
            kotlinx.coroutines.delay(1000)
        }
    }

    val canBuy = uiState.remainingLocked > 0 && points >= 10
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBarText() },
        bottomBar = { NavigationBar(navController = navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(id = R.string.bingoboard_title),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = remainingReset,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = stringResource(id = R.string.bingoboard_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Surface(
                tonalElevation = 2.dp,
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.bingoboard_points_available, points),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Button(
                        onClick = onBuyRandomField,
                        enabled = canBuy,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.bingoboard_action_buy))
                    }
                    AnimatedVisibility(visible = !canBuy && points < 10 && uiState.remainingLocked > 0) {
                        Text(
                            text = stringResource(id = R.string.bingoboard_not_enough_points),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    AnimatedVisibility(visible = uiState.remainingLocked == 0) {
                        Text(
                            text = stringResource(id = R.string.bingoboard_locked_finished),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            BingoGrid(uiState.cells)
        }
    }
}

@Composable
private fun BingoGrid(cells: List<BingoCell>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        cells.chunked(3).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { cell ->
                    BingoCellCard(cell = cell)
                }
            }
        }
    }
}

@Composable
private fun RowScope.BingoCellCard(cell: BingoCell) {
    val unlocked = cell.unlocked
    Surface(
        modifier = Modifier
            .weight(1f)
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        tonalElevation = if (unlocked) 4.dp else 1.dp,
        color = if (unlocked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                AnimatedVisibility(visible = unlocked && cell.stickerResId != null) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Image(
                            painter = painterResource(id = cell.stickerResId!!),
                            contentDescription = null,
                            modifier = Modifier
                                .size(56.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Bingo Light", showBackground = true)
@Composable
private fun BingoPreviewLight() {
    val previewNavController = rememberPreviewNavController()
    val sampleState = BingoUiState(
        cells = listOf(
            BingoCell(0, "Feld 1", unlocked = true, stickerResId = R.drawable.sticker_redpanda_01),
            BingoCell(1, "Feld 2"),
            BingoCell(2, "Feld 3"),
            BingoCell(3, "Feld 4"),
            BingoCell(4, "Feld 5", unlocked = true, stickerResId = R.drawable.sticker_skzoo_01),
            BingoCell(5, "Feld 6"),
            BingoCell(6, "Feld 7"),
            BingoCell(7, "Feld 8"),
            BingoCell(8, "Feld 9")
        )
    )
    LumakaTheme {
        BingoBoardView(
            navController = previewNavController,
            uiState = sampleState,
            points = 25,
            onBuyRandomField = {},
            onWeekTick = {}
        )
    }
}

@Preview(name = "Bingo Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun BingoPreviewDark() {
    val previewNavController = rememberPreviewNavController()
    val sampleState = BingoUiState(
        cells = listOf(
            BingoCell(0, "Feld 1", unlocked = true, stickerResId = R.drawable.sticker_redpanda_01),
            BingoCell(1, "Feld 2"),
            BingoCell(2, "Feld 3"),
            BingoCell(3, "Feld 4"),
            BingoCell(4, "Feld 5", unlocked = true, stickerResId = R.drawable.sticker_skzoo_01),
            BingoCell(5, "Feld 6"),
            BingoCell(6, "Feld 7"),
            BingoCell(7, "Feld 8"),
            BingoCell(8, "Feld 9", unlocked = true, stickerResId = R.drawable.sticker_skzoo_02)
        )
    )
    LumakaTheme {
        BingoBoardView(
            navController = previewNavController,
            uiState = sampleState,
            points = 25,
            onBuyRandomField = {},
            onWeekTick = {}
        )
    }
}

private fun nextResetMonday(zone: ZoneId, now: ZonedDateTime): ZonedDateTime {
    val currentMonday = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .toLocalDate()
        .atStartOfDay(zone)
    return currentMonday.plusWeeks(1)
}

private fun formatDuration(duration: Duration): String {
    val totalSeconds = duration.seconds.coerceAtLeast(0)
    val days = totalSeconds / 86_400
    val hours = (totalSeconds % 86_400) / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return if (days > 0) {
        "%dd %02d:%02d:%02d".format(days, hours, minutes, seconds)
    } else {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    }
}
