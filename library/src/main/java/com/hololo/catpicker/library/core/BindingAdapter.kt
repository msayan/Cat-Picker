package com.hololo.catpicker.library.core

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object BindingAdapter {

    @JvmStatic
    @BindingAdapter("loadImage")
    fun loadImage(view: ImageView, url: String?) {
        if (url.isNullOrBlank()) {
            return
        }

        with(Picasso.get()) {
            cancelRequest(view)
            load(url).fit().centerInside().into(view)
        }
    }
}
