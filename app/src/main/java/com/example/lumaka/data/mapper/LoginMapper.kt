package com.example.lumaka.data.mapper

import com.example.lumaka.data.remote.dto.LoginDTO
import com.example.lumaka.domain.model.Login

fun Login.toDTO(): LoginDTO{
    return LoginDTO(
        mail = this.mail,
        password = this.password
    )
}
