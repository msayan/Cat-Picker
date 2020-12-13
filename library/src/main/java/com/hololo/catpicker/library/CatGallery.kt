package com.hololo.catpicker.library

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.hololo.catpicker.library.CatGallery.Builder
import com.hololo.catpicker.library.core.Constants
import com.hololo.catpicker.library.domain.CatImageModel
import com.hololo.catpicker.library.ui.CatGalleryActivity

/**
 * CatGallery helps user to pick a Cat image from
 * [The Cat API](http://thecatapi.com/)
 *
 * Define how toolbar and back icon looks.
 * Create an instance using [Builder]
 */
class CatGallery private constructor(
    private val activity: Activity,
    private val title: String?,
    private val backIconDrawable: Int?,
    private val toolbarColor: Int?,
    private val listener: ((Uri) -> Unit)?
) {

    /**
     * Opens Cat gallery activity
     */
    fun show() {
        val intent = Intent(activity, CatGalleryActivity::class.java).apply {
            putExtra(Constants.IntentName.TOOLBAR_COLOR, toolbarColor)
            putExtra(Constants.IntentName.BACK_ICON, backIconDrawable)
            putExtra(Constants.IntentName.TITLE, title)
        }

        activity.startActivityForResult(intent, Constants.REQUEST_CODE)
    }

    /**
     * Handles [onActivityResult] and invoke callback with [CatImageModel]
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.getParcelableExtra<Uri>(Constants.IntentName.IMAGE_FILE)
            imageUri?.let { listener?.invoke(it) }
        }
    }

    /**
     * Build a new [CatGallery].
     *
     * Calling [setActivity] is required before calling [build].
     * All other methods are optional.
     */
    class Builder {
        private var activity: Activity? = null
        private var title: String = ""
        private var titleRes: Int = -1
        private var backIconDrawable: Int = -1
        private var toolbarColor: Int = -1
        private var listener: ((Uri) -> Unit)? = null

        /**
         * Setter function for activity
         * @param activity Activity for starting new Activity
         * @return [Builder]
         */
        fun setActivity(activity: Activity) = apply {
            this.activity = activity
        }

        /**
         * Setter function for toolbar title
         * @param title Toolbar title
         * @return [Builder]
         */
        fun setTitle(title: String) = apply {
            this.title = title
        }

        /**
         * Setter function for toolbar title String Resource
         * @param title Toolbar title String Resource
         * @return [Builder]
         */
        fun setTitle(@StringRes title: Int) = apply {
            this.titleRes = title
        }

        /**
         * Setter function for back button icon
         * @param backIconDrawable Back icon Drawable Resource
         * @return [Builder]
         */
        fun setBackIconDrawable(@DrawableRes backIconDrawable: Int) = apply {
            this.backIconDrawable = backIconDrawable
        }

        /**
         * Setter function for toolbar color
         * @param toolbarColor Toolbar Color Resource
         * @return [Builder]
         */
        fun setToolbarColor(@ColorRes toolbarColor: Int) = apply {
            this.toolbarColor = toolbarColor
        }

        /**
         * Setter function for result listener
         * @param listener Result listener
         * @return [Builder]
         */
        fun setImageListener(listener: ((Uri) -> Unit)) = apply {
            this.listener = listener
        }

        /**
         * Builds an instance of [CatGallery]
         * @return [CatGallery]
         */
        fun build(): CatGallery {
            if (activity == null) {
                throw IllegalArgumentException("You must set an activity with setActivity function")
            }

            return CatGallery(
                activity!!,
                if (titleRes != -1) try {
                    activity?.getString(titleRes)
                } catch (ex: Resources.NotFoundException) {
                    ""
                } else title,
                backIconDrawable,
                toolbarColor,
                listener
            )
        }
    }
}
