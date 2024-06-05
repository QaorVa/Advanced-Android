package com.example.storiesw.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storiesw.DataDummy
import com.example.storiesw.MainDispatcherRule
import com.example.storiesw.data.model.Story
import com.example.storiesw.data.repository.StoryRepository
import com.example.storiesw.getOrAwaitValue
import com.example.storiesw.ui.adapter.HomeAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository


    @Before
    fun setup() {
        storyRepository = Mockito.mock(StoryRepository::class.java)
    }


    @Test
    fun `when Get Story Should Not Null and Return Data`() = runTest {
        val dummyStory = DataDummy.generateDummyStory()
        val data = StoryPagingSourceTest.snapshot(dummyStory)

        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data

        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val differ = AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        val homeViewModel = HomeViewModel(storyRepository)
        val actualStory = homeViewModel.story.getOrAwaitValue()

        differ.submitData(actualStory)

        assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Story Empty Should Return No Data`() = runTest {
        val data: PagingData<Story> = PagingData.from(emptyList())
        val expectedStory = MutableLiveData<PagingData<Story>>()
        expectedStory.value = data
        Mockito.`when`(storyRepository.getStories()).thenReturn(expectedStory)

        val homeViewModel = HomeViewModel(storyRepository)
        val actualStory = homeViewModel.story.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = HomeAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )

        differ.submitData(actualStory)
        assertEquals(0, differ.snapshot().size)
    }

    class StoryPagingSourceTest : PagingSource<Int, LiveData<List<Story>>>() {
        override fun getRefreshKey(state: PagingState<Int, LiveData<List<Story>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<Story>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }

        companion object {
            fun snapshot(items: List<Story>): PagingData<Story> {
                return PagingData.from(items)
            }
        }

    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}