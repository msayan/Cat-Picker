package com.hololo.catpicker.library.ui

import android.app.Application
import android.content.Intent
import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.hololo.catpicker.library.R
import com.hololo.catpicker.library.api.CatAPI
import com.hololo.catpicker.library.api.FakeAPI
import com.hololo.catpicker.library.api.ImageFactory
import com.hololo.catpicker.library.util.DefaultServiceLocator
import com.hololo.catpicker.library.util.ServiceLocator
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class CatGalleryActivityTest {
    @get:Rule
    var testRule = CountingTaskExecutorRule()

    private val imageFactory = ImageFactory()
    private val pageSize = 20
    private val fakeApi = FakeAPI()

    @Before
    fun init() {
        fakeApi.addImage(imageFactory.createImages(pageSize), 0)
        fakeApi.addImage(imageFactory.createImages(pageSize), 1)
        val app = ApplicationProvider.getApplicationContext<Application>()
        ServiceLocator.swap(
            object : DefaultServiceLocator(app = app) {
                override fun getCatApi(): CatAPI = fakeApi
            }
        )
    }

    @Test
    @Throws(InterruptedException::class, TimeoutException::class)
    fun showCatImages() {
        val intent =
            Intent(ApplicationProvider.getApplicationContext(), CatGalleryActivity::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val activity = InstrumentationRegistry.getInstrumentation().startActivitySync(intent)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.cat_recycler)
        assertThat(recyclerView.adapter, notNullValue())
        waitForAdapterChange(recyclerView)
        assertThat(recyclerView.adapter?.itemCount, `is`(40))
    }

    private fun waitForAdapterChange(recyclerView: RecyclerView) {
        val latch = CountDownLatch(1)
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            recyclerView.adapter?.registerAdapterDataObserver(
                object : RecyclerView.AdapterDataObserver() {
                    override fun onChanged() {
                        latch.countDown()
                    }

                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        latch.countDown()
                    }
                }
            )
        }
        testRule.drainTasks(1, TimeUnit.SECONDS)
        if (recyclerView.adapter?.itemCount ?: 0 > 0) {
            return
        }
        assertThat(latch.await(10, TimeUnit.SECONDS), `is`(true))
    }
}
