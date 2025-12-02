package com.example.lumaka.ui.feature.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lumaka.R
import com.example.lumaka.domain.model.User
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
                        text = user?.email ?: "lumaka@example.com",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileStat(
                        label = stringResource(id = R.string.profile_points),
                        value = "1 250"
                    )
                    ProfileStat(
                        label = stringResource(id = R.string.profile_badges),
                        value = "5"
                    )
                    ProfileStat(
                        label = stringResource(id = R.string.profile_tasks_completed),
                        value = "42"
                    )
                }
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
