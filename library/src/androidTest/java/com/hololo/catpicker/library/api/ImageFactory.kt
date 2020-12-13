package com.hololo.catpicker.library.api

import com.hololo.catpicker.library.api.response.ImageResponse
import java.util.concurrent.atomic.AtomicInteger

class ImageFactory {
    private val counter = AtomicInteger(0)

    fun createImages(pageSize: Int): List<ImageResponse> {
        val images = mutableListOf<ImageResponse>()
        for (i in 1..pageSize) {
            val id = counter.incrementAndGet()
            val imageResponse = ImageResponse(350, "id", "$id.jpg", 350)
            images.add(imageResponse)
        }
        return images
    }
}
