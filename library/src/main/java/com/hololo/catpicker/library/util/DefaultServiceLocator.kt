package com.hololo.catpicker.library.util

import android.app.Application
import com.hololo.catpicker.library.api.CatAPI
import com.hololo.catpicker.library.domain.CatImageMapper
import com.hololo.catpicker.library.repo.CatRepo

open class DefaultServiceLocator(val app: Application) : ServiceLocator {
    private val api by lazy {
        CatAPI.create()
    }

    private val mapper by lazy {
        CatImageMapper()
    }

    override fun getCatRepo(): CatRepo {
        return CatRepo(getCatApi(), mapper)
    }

    override fun getCatApi(): CatAPI {
        return api
    }
}
