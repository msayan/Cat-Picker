package com.hololo.catpicker.library.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hololo.catpicker.library.api.CatAPI
import com.hololo.catpicker.library.core.Constants
import com.hololo.catpicker.library.domain.CatImageMapper

class CatRepo(private val api: CatAPI, private val mapper: CatImageMapper) {

    fun getImages() = Pager(
        PagingConfig(pageSize = Constants.PAGE_SIZE, maxSize = 150, enablePlaceholders = true),
        initialKey = 0
    ) {
        CatPagingSource(api, mapper)
    }.flow
}
