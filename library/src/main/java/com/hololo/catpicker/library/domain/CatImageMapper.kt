package com.hololo.catpicker.library.domain

import com.hololo.catpicker.library.api.response.ImageResponse

class CatImageMapper {

    fun apply(response: List<ImageResponse>): List<CatImageModel> {
        return response.map { CatImageModel(it.url) }
    }
}
