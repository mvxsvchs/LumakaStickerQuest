package com.example.lumaka.data.mapper

import com.example.lumaka.data.remote.dto.RegisterDTO
import com.example.lumaka.domain.model.Registration

fun Registration.toDTO(): RegisterDTO {
    return RegisterDTO(
        username = this.username,
        mail = this.mail,
        password = this.password
    )
}