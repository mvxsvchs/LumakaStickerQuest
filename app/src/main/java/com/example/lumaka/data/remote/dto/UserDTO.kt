package com.example.lumaka.data.remote.dto

import com.squareup.moshi.Json

data class UserDTO(
    @Json(name = "username") val username: String,
    @Json(name = "userId") val userId: Int = 0,
    @Json(name = "user_id") val userIdSnake: Int = 0,
    @Json(name = "points") val points: Int = 0,
    @Json(name = "stickerId") val stickerId: List<Int> = emptyList(),
    @Json(name = "sticker_id") val stickerIdSnake: List<Int> = emptyList(),
    @Json(name = "email") val email: String? = null,
    @Json(name = "mail") val mail: String? = null,
    @Json(name = "avatarId") val avatarId: Int? = null,
    @Json(name = "avatar_id") val avatarIdSnake: Int? = null,
) {
    val resolvedUserId: Int get() = if (userId != 0) userId else userIdSnake
    val resolvedStickerIds: List<Int> get() = if (stickerId.isNotEmpty()) stickerId else stickerIdSnake
    val resolvedEmail: String? get() = email ?: mail
    val resolvedAvatarId: Int? get() = avatarId ?: avatarIdSnake
}
