package com.hololo.catpicker.library.util

import android.app.Application
import android.content.Context
import androidx.annotation.VisibleForTesting
import com.hololo.catpicker.library.api.CatAPI
import com.hololo.catpicker.library.repo.CatRepo

interface ServiceLocator {
    companion object {
        private val LOCK = Any()
        private var instance: ServiceLocator? = null
        fun instance(context: Context): ServiceLocator {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = DefaultServiceLocator(
                        app = context.applicationContext as Application
                    )
                }
                return instance!!
            }
        }

        /**
         * Allows tests to replace the default implementations.
         */
        @VisibleForTesting
        fun swap(locator: ServiceLocator) {
            instance = locator
        }
    }

    fun getCatRepo(): CatRepo

    fun getCatApi(): CatAPI
}
