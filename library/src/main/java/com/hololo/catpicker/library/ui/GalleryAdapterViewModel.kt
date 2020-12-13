package com.hololo.catpicker.library.ui

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.hololo.catpicker.library.domain.CatImageModel

class GalleryAdapterViewModel : ViewModel() {
    val model = ObservableField<CatImageModel>()
}
