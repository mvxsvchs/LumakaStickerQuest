package com.example.lumaka.data.remote.dto

import com.squareup.moshi.Json

data class StickerIdRequest(
    @Json(name = "StickerId")
    val stickerId: Int
)
