package com.hololo.catpicker.library.ui

import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.hololo.catpicker.library.databinding.ItemLoadStateBinding

class LoadStateViewHolder(
    binding: ItemLoadStateBinding,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    binding.root
) {
    private val progressBar: ProgressBar = binding.progressBar
    private val errorMsg: TextView = binding.errorMsg
    private val retry: Button = binding.retryButton
        .also {
            it.setOnClickListener { retry() }
        }

    fun bind(loadState: LoadState) {
        Log.e("bind", "loadState is $loadState")
        if (loadState is LoadState.Error) {
            errorMsg.text = loadState.error.localizedMessage
        }

        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error || loadState is LoadState.NotLoading
        errorMsg.isVisible = loadState is LoadState.Error || loadState is LoadState.NotLoading
    }
}
