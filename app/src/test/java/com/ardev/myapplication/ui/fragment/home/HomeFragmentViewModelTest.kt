package com.ardev.myapplication.ui.fragment.home

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.test.core.app.ApplicationProvider
import com.ardev.myapplication.DataDummy
import com.ardev.myapplication.ListStoryItemDiffCallback
import com.ardev.myapplication.data.response.ListStoryItem
import com.ardev.myapplication.data.response.StoryResponse
import com.ardev.myapplication.data.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeFragmentViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var apiService: ApiService

    private lateinit var viewModel: HomeFragmentViewModel
    private val dummyToken = "Bearer dummy_token"

    @Before
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        viewModel = HomeFragmentViewModel(application, apiService)
    }

    @Test
    fun `when getStories should not be null and return success`() = runTest {
        val dummyStories = DataDummy.getDummyData()
        val dummyPagingData = PagingData.from(dummyStories)
        val expectedStories: Flow<PagingData<ListStoryItem>> = flowOf(dummyPagingData)

        Mockito.`when`(viewModel.getStories(dummyToken)).thenReturn(expectedStories)

        val actualStories = viewModel.getStories(dummyToken)
        assertNotNull(actualStories)

        actualStories.collectLatest { pagingData ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = ListStoryItemDiffCallback(),
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(pagingData)

            assertEquals(dummyStories.size, differ.snapshot().size)
            assertEquals(dummyStories[0], differ.snapshot()[0])
        }
    }

    @Test
    fun `when getStories with no data should return zero items`() = runTest {
        val emptyPagingData = PagingData.empty<ListStoryItem>()
        val expectedStories: Flow<PagingData<ListStoryItem>> = flowOf(emptyPagingData)

        Mockito.`when`(viewModel.getStories(dummyToken)).thenReturn(expectedStories)

        val actualStories = viewModel.getStories(dummyToken)
        assertNotNull(actualStories)

        actualStories.collectLatest { pagingData ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = ListStoryItemDiffCallback(),
                updateCallback = noopListUpdateCallback,
                workerDispatcher = Dispatchers.Main
            )

            differ.submitData(pagingData)

            assertEquals(0, differ.snapshot().size)
        }
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            println("onInserted: position = $position, count = $count")
        }
        override fun onRemoved(position: Int, count: Int) {
            println("onRemoved: position = $position, count = $count")
        }
        override fun onMoved(fromPosition: Int, toPosition: Int) {
            println("onMoved: fromPosition = $fromPosition, toPosition = $toPosition")
        }
        override fun onChanged(position: Int, count: Int, payload: Any?) {
            println("onChanged: position = $position, count = $count, payload = $payload")
        }
    }
}
