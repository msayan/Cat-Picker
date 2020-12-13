package com.hololo.catpicker.library.ui

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hololo.catpicker.library.R
import com.hololo.catpicker.library.databinding.ItemCatBinding
import com.hololo.catpicker.library.domain.CatImageModel

class GalleryAdapter(private val callback: (Bitmap) -> Unit) :
    PagingDataAdapter<CatImageModel, GalleryAdapter.ViewHolder>(
        object : DiffUtil.ItemCallback<CatImageModel>() {
            override fun areItemsTheSame(oldItem: CatImageModel, newItem: CatImageModel): Boolean {
                return oldItem.url == newItem.url
            }

            override fun areContentsTheSame(
                oldItem: CatImageModel,
                newItem: CatImageModel
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCatBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cat,
            parent,
            false
        )
        with(binding) {
            viewModel = GalleryAdapterViewModel()
            image.setOnClickListener {
                (image.drawable as? BitmapDrawable)?.bitmap?.let { bitmap -> callback.invoke(bitmap) }
            }
        }
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            viewBinding.viewModel?.model?.set(getItem(position))
            viewBinding.executePendingBindings()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    inner class ViewHolder(val viewBinding: ItemCatBinding) :
        RecyclerView.ViewHolder(viewBinding.root)
}
