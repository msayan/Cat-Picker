package com.hololo.catpicker.library.domain

import com.hololo.catpicker.library.util.MockData
import org.junit.Assert.assertEquals
import org.junit.Test

class CatImageMapperTest {
    private val mapper = CatImageMapper()

    @Test
    fun test_responseToImageModel() {
        val list = MockData.responseList
        val imageModelList = mapper.apply(list)

        assertEquals(imageModelList, MockData.imageList)
    }
}
