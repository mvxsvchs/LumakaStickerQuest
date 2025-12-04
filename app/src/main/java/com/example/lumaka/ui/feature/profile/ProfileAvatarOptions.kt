package com.example.lumaka.ui.feature.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Pets
import androidx.compose.material.icons.rounded.Person
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class AvatarOption(
    val id: Int,
    val icon: ImageVector,
    val background: Color,
    val contentColor: Color
)

val DefaultAvatarOptions = listOf(
    AvatarOption(
        id = 1,
        icon = Icons.Rounded.Person,
        background = Color(0xFF5F8BFF),
        contentColor = Color(0xFFFFFFFF)
    ),
    AvatarOption(
        id = 2,
        icon = Icons.Rounded.Bolt,
        background = Color(0xFF4EC7A1),
        contentColor = Color(0xFF0B241A)
    ),
    AvatarOption(
        id = 3,
        icon = Icons.Rounded.Pets,
        background = Color(0xFFFF90B6),
        contentColor = Color(0xFF2A0E1A)
    ),
    AvatarOption(
        id = 4,
        icon = Icons.Rounded.AutoAwesome,
        background = Color(0xFF8E8FFF),
        contentColor = Color(0xFF0E1033)
    ),
    AvatarOption(
        id = 5,
        icon = Icons.Rounded.Lightbulb,
        background = Color(0xFF7ED4F4),
        contentColor = Color(0xFF082432)
    )
)
