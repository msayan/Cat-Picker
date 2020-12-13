package com.hololo.catpicker.library.util

import com.hololo.catpicker.library.api.response.ImageResponse
import com.hololo.catpicker.library.domain.CatImageModel

object MockData {
    val responseList = listOf(
        ImageResponse(350, "1", "1.jpg", 350),
        ImageResponse(350, "2", "2.jpg", 350),
        ImageResponse(350, "3", "3.jpg", 350),
        ImageResponse(350, "4", "4.jpg", 350),
        ImageResponse(350, "5", "5.jpg", 350),
        ImageResponse(350, "6", "6.jpg", 350),
        ImageResponse(350, "7", "7.jpg", 350),
        ImageResponse(350, "8", "8.jpg", 350)
    )

    val imageList = listOf(
        CatImageModel("1.jpg"),
        CatImageModel("2.jpg"),
        CatImageModel("3.jpg"),
        CatImageModel("4.jpg"),
        CatImageModel("5.jpg"),
        CatImageModel("6.jpg"),
        CatImageModel("7.jpg"),
        CatImageModel("8.jpg")
    )
}
