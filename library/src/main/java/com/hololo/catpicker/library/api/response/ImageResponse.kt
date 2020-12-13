package com.hololo.catpicker.library.api.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageResponse(
    @field:Json(name = "height")
    val height: Long?,
    @field:Json(name = "id")
    val id: String?,
    @field:Json(name = "url")
    val url: String?,
    @field:Json(name = "width")
    val width: Long?
)
