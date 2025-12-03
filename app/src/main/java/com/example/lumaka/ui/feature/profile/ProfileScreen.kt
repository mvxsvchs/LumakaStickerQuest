package com.example.lumaka.ui.feature.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lumaka.R
import com.example.lumaka.domain.model.User
import com.example.lumaka.ui.feature.bingo.StickerAssets
import com.example.lumaka.ui.component.NavigationBar
import com.example.lumaka.ui.component.TopBarText
import com.example.lumaka.ui.theme.LumakaTheme
import com.example.lumaka.util.rememberPreviewNavController
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ProfileRoute(
    navController: NavController,
    profileViewModel: ProfileViewModel = hiltViewModel()
) {
    val userState = profileViewModel.user.collectAsState()
    ProfileView(navController = navController, user = userState.value)
}

@Composable
fun ProfileView(
    navController: NavController,
    user: User?
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = { TopBarText() },
        bottomBar = { NavigationBar(navController = navController) }
    ){ padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = user?.username ?: stringResource(id = R.string.profile_title),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = user?.email ?: stringResource(id = R.string.profile_email_placeholder),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val stickerResIds = user?.stickerid?.mapNotNull { StickerAssets.resIdFor(it) } ?: emptyList()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val animatedPoints by animateIntAsState(
                        targetValue = user?.points ?: 0,
                        animationSpec = tween(durationMillis = 500),
                        label = "profilePoints"
                    )
                    ProfileStat(
                        label = stringResource(id = R.string.profile_points),
                        value = animatedPoints.toString()
                    )
                    ProfileStat(
                        label = stringResource(id = R.string.profile_stickers),
                        value = stickerResIds.size.toString()
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                StickerGrid(stickerResIds = user?.stickerid ?: emptyList())
            }
        }
    }
}

@Composable
private fun ProfileStat(label: String, value: String){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StickerGrid(stickerResIds: List<Int>) {
    val resolved = stickerResIds.mapNotNull { StickerAssets.resIdFor(it) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(id = R.string.profile_stickers),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        if (resolved.isEmpty()) {
            Text(
                text = stringResource(id = R.string.profile_stickers_empty),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                items(resolved) { resId ->
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        androidx.compose.foundation.Image(
                            painter = androidx.compose.ui.res.painterResource(id = resId),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Profile Light", showBackground = true)
@Composable
private fun ProfilePreviewLight(){
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        ProfileView(
            navController = previewNavController,
            user = User(username = "Lumaka User", userid = 1, points = 1250, stickerid = emptyList(), email = "lumaka@example.com")
        )
    }
}

@Preview(name = "Profile Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfilePreviewDark(){
    val previewNavController = rememberPreviewNavController()
    LumakaTheme {
        ProfileView(
            navController = previewNavController,
            user = User(username = "Lumaka User", userid = 1, points = 1250, stickerid = emptyList(), email = "lumaka@example.com")
        )
    }
}
