package com.hololo.catpicker.library.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CatImageModel(
    val url: String?
) : Parcelable
