package com.hololo.catpicker.sample

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.hololo.catpicker.library.CatGallery

class MainActivity : AppCompatActivity() {

    private lateinit var gallery: CatGallery
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.image_view)
        val pickButton = findViewById<View>(R.id.pick_image_button)

        gallery =
            CatGallery.Builder().setActivity(this)
                .setTitle("Test")
                .setToolbarColor(R.color.purple_500)
                .setBackIconDrawable(R.drawable.ic_baseline_chevron_left)
                .setImageListener {
                    imageView.setImageURI(it)
                }
                .build()

        pickButton.setOnClickListener {
            gallery.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        gallery.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}