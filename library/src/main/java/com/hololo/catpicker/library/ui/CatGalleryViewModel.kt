package com.hololo.catpicker.library.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.hololo.catpicker.library.repo.CatRepo

class CatGalleryViewModel(private val repo: CatRepo) : ViewModel() {
    fun getImages() = repo.getImages().cachedIn(viewModelScope)
}
