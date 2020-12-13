package com.hololo.catpicker.library.repo

import androidx.paging.PagingSource
import com.hololo.catpicker.library.api.CatAPI
import com.hololo.catpicker.library.core.Constants
import com.hololo.catpicker.library.domain.CatImageMapper
import com.hololo.catpicker.library.domain.CatImageModel
import retrofit2.HttpException
import java.io.IOException

class CatPagingSource(private val api: CatAPI, private val mapper: CatImageMapper) :
    PagingSource<Int, CatImageModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CatImageModel> {
        return try {
            val response = api.getImages(params.key ?: 0, Constants.PAGE_SIZE)
            LoadResult.Page(
                data = mapper.apply(response),
                nextKey = if (response.size < Constants.PAGE_SIZE) null else (params.key ?: 0).plus(
                    1
                ),
                prevKey = if (params.key ?: 0 > 0) params.key?.minus(1) ?: 0 else null,
                itemsAfter = if (response.size < Constants.PAGE_SIZE) 0 else Constants.PAGE_SIZE
            )
        } catch (ex: IOException) {
            LoadResult.Error(ex)
        } catch (ex: HttpException) {
            LoadResult.Error(ex)
        }
    }
}
