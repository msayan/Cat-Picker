package com.hololo.catpicker.library.api

import com.hololo.catpicker.library.api.response.ImageResponse
import java.io.IOException

class FakeAPI : CatAPI {
    private val model = mutableMapOf<Int, ArrayList<ImageResponse>>()
    private var errorMessage: String? = null

    fun setErrorMessage(errorMessage: String) {
        this.errorMessage = errorMessage
    }

    fun addImage(imageList: List<ImageResponse>, page: Int) {
        val images = model.getOrPut(page) {
            arrayListOf()
        }
        images.addAll(imageList)
    }

    override suspend fun getImages(
        page: Int?,
        limit: Int,
        size: String,
        order: String
    ): List<ImageResponse> {
        if (errorMessage != null) throw IOException()

        if (page == null || page < 0) return emptyList()

        return model[page] ?: emptyList()
    }
}
