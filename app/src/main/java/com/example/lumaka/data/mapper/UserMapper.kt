package com.example.lumaka.data.mapper

import com.example.lumaka.data.remote.dto.UserDTO
import com.example.lumaka.domain.model.User

fun UserDTO.toDomain(): User{
    return User(
        username = this.username,
        userid = this.userid,
        points = this.points,
        stickerid = this.stickerid
    )
}
