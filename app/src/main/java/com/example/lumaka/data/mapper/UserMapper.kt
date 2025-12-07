package com.example.lumaka.data.mapper

import com.example.lumaka.data.remote.dto.UserDTO
import com.example.lumaka.domain.model.User

fun UserDTO.toDomain(fallbackEmail: String = ""): User {
    return User(
        username = this.username,
        userid = this.resolvedUserId,
        points = this.resolvedPoints,
        stickerid = this.resolvedStickerIds,
        email = this.resolvedEmail ?: fallbackEmail,
        avatarId = this.resolvedAvatarId ?: 1,
    )
}
