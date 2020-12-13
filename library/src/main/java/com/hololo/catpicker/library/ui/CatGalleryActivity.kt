package com.hololo.catpicker.library.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import com.hololo.catpicker.library.R
import com.hololo.catpicker.library.core.Constants
import com.hololo.catpicker.library.databinding.ActivityCatGalleryBinding
import com.hololo.catpicker.library.util.ConnectionUtil
import com.hololo.catpicker.library.util.ServiceLocator
import kotlinx.coroutines.flow.collectLatest
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import kotlin.math.roundToInt

class CatGalleryActivity : AppCompatActivity() {

    private val model: CatGalleryViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this, null) {
            override fun <T : ViewModel?> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val repo = ServiceLocator.instance(this@CatGalleryActivity).getCatRepo()

                @Suppress("UNCHECKED_CAST")
                return CatGalleryViewModel(repo) as T
            }
        }
    }

    private lateinit var binding: ActivityCatGalleryBinding
    private lateinit var connectionUtil: ConnectionUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cat_gallery)
        connectionUtil = ConnectionUtil.getInstance(this)
        connectionUtil.observe(
            this,
            {
                if (it) {
                    init()
                    parseIntent()
                    binding.errorText.visibility = View.GONE
                    binding.viewContainer.visibility = View.VISIBLE
                    connectionUtil.removeObservers(this)
                } else {
                    binding.errorText.visibility = View.VISIBLE
                    binding.viewContainer.visibility = View.GONE
                }
            }
        )
    }

    private fun parseIntent() {
        val toolbarColor = intent.getIntExtra(Constants.IntentName.TOOLBAR_COLOR, -1)
        val backIcon = intent.getIntExtra(Constants.IntentName.BACK_ICON, -1)
        val title = intent.getStringExtra(Constants.IntentName.TITLE)

        if (toolbarColor != -1) {
            supportActionBar?.setBackgroundDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        this,
                        toolbarColor
                    )
                )
            )

            window.statusBarColor = makeColorDarker(
                ContextCompat.getColor(
                    this,
                    toolbarColor
                )
            )
        }

        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        if (backIcon != -1) {
            supportActionBar?.setHomeAsUpIndicator(backIcon)
        }
    }

    private fun makeColorDarker(color: Int): Int {
        val factor = .8f
        val a: Int = Color.alpha(color)
        val r = (Color.red(color) * factor).roundToInt()
        val g = (Color.green(color) * factor).roundToInt()
        val b = (Color.blue(color) * factor).roundToInt()
        return Color.argb(
            a,
            r.coerceAtMost(255),
            g.coerceAtMost(255),
            b.coerceAtMost(255)
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun init() {
        setSupportActionBar(binding.toolbar)

        val adapter = GalleryAdapter { bitmap ->
            saveFile(bitmap)?.let { file ->
                val intent = Intent()
                intent.putExtra(Constants.IntentName.IMAGE_FILE, Uri.fromFile(file))
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
        with(adapter) {
            binding.catRecycler.adapter = this.withLoadStateFooter(
                footer = CatRecyclerLoadStateAdapter { retry() }
            )

            (binding.catRecycler.layoutManager as GridLayoutManager).spanSizeLookup =
                object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return when {
                            (binding.catRecycler.adapter as ConcatAdapter).getItemViewType(
                                position
                            ) == 0 -> 1
                            else -> 3
                        }
                    }
                }

            binding.catRecycler.setHasFixedSize(true)

            binding.swipeRefresh.setOnRefreshListener {
                refresh()
            }

            lifecycleScope.launchWhenCreated {
                loadStateFlow.collectLatest { loadStates ->
                    binding.swipeRefresh.isRefreshing = loadStates.refresh is LoadState.Loading
                }
            }

            lifecycleScope.launchWhenCreated {
                model.getImages().collectLatest {
                    submitData(it)
                }
            }
        }
    }

    private fun saveFile(bitmap: Bitmap): File? {
        return try {
            val file = File(cacheDir, "${UUID.randomUUID()}.jpg")
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
            file
        } catch (ex: Exception) {
            Toast.makeText(this, "Oops, we fucked up! Please try again!", Toast.LENGTH_LONG)
                .show()
            null
        }
    }
}
