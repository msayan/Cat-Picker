package com.hololo.catpicker.library.repo

import androidx.paging.*
import com.hololo.catpicker.library.api.CatAPI
import com.hololo.catpicker.library.core.Constants
import com.hololo.catpicker.library.domain.CatImageMapper
import com.hololo.catpicker.library.domain.CatImageModel
import com.hololo.catpicker.library.util.MockData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.setMain
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CatRepoTest {

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    private val api = mockk<CatAPI>()
    private val mapper = CatImageMapper()

    private val repo = CatRepo(api, mapper)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testSuccess(): Unit = runBlocking {
        coEvery { api.getImages(0, Constants.PAGE_SIZE) } returns MockData.responseList
        val list = mutableListOf<CatImageModel>()
        val data = repo.getImages().first()
        val differ = object : PagingDataDiffer<CatImageModel>(
            object : DifferCallback {
                override fun onChanged(position: Int, count: Int) {
                }

                override fun onInserted(position: Int, count: Int) {
                }

                override fun onRemoved(position: Int, count: Int) {
                }
            }
        ) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<CatImageModel>,
                newList: NullPaddedList<CatImageModel>,
                newCombinedLoadStates: CombinedLoadStates,
                lastAccessedIndex: Int
            ): Int {
                for (i in 0 until newList.size) {
                    list.add(newList.getFromStorage(i))
                }
                return lastAccessedIndex
            }

        }
        testScope.launch {
            differ.collectFrom(data)
        }
        assertThat(list).isEqualTo(MockData.imageList)
    }

    @Test
    fun testFailure() = runBlocking {
        coEvery { api.getImages(0, Constants.PAGE_SIZE) } throws IOException()

        val data = repo.getImages().first()
        val differ = object : PagingDataDiffer<CatImageModel>(
            object : DifferCallback {
                override fun onChanged(position: Int, count: Int) {
                }

                override fun onInserted(position: Int, count: Int) {
                }

                override fun onRemoved(position: Int, count: Int) {
                }

            }
        ) {
            override suspend fun presentNewList(
                previousList: NullPaddedList<CatImageModel>,
                newList: NullPaddedList<CatImageModel>,
                newCombinedLoadStates: CombinedLoadStates,
                lastAccessedIndex: Int
            ): Int {
                return lastAccessedIndex
            }

        }
        testScope.launch {
            differ.collectFrom(data)
        }

        val loadState = differ.loadStateFlow.first()
        assert(loadState.source.refresh is LoadState.Error)
    }
}
