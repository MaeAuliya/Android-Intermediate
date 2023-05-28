package com.example.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.utils.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var mainViewModel: MainViewModel


    @Before
    fun setUp(){
        mainViewModel = MainViewModel(storyRepository)
    }


    @Test
    fun `when Get Story List should not null, data size matched and first returned data is correct`() = runTest {

        val dataDummy = DataDummy.generateDummyStoryData()
        val data = PagedTestDataSource.snapShot(dataDummy)

        val expectedStory = MutableLiveData<PagingData<StoryModel>>()
        expectedStory.value = data

        `when` (storyRepository.loadStories()).thenReturn(expectedStory)

        val actualStory = mainViewModel.listStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            StoryAdapter.DIFF_CALLBACK,
            listUpdateCallback,
            mainDispatcherRule.testDispatcher,
            mainDispatcherRule.testDispatcher
        )
        differ.submitData(actualStory)
        advanceUntilIdle()

        verify(storyRepository).loadStories()


        assertNotNull(differ.snapshot())
        assertEquals(dataDummy.size, differ.snapshot().size)
        assertEquals(dataDummy[0], differ.snapshot()[0])

    }

    @Test
    fun `should return 0 when Get Story List is empty`() = runTest {

        val data = PagedTestDataSource.snapShot(emptyList())

        val expectedStory = MutableLiveData<PagingData<StoryModel>>()
        expectedStory.value = data

        `when` (storyRepository.loadStories()).thenReturn(expectedStory)

        val actualStory = mainViewModel.listStories().getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            StoryAdapter.DIFF_CALLBACK,
            listUpdateCallback,
            mainDispatcherRule.testDispatcher,
            mainDispatcherRule.testDispatcher
        )
        differ.submitData(actualStory)
        advanceUntilIdle()

        verify(storyRepository).loadStories()

        assertNotNull(differ.snapshot())
        assertEquals(0, differ.snapshot().size)
        assertTrue(differ.snapshot().isEmpty())
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}

        override fun onRemoved(position: Int, count: Int) {}

        override fun onMoved(fromPosition: Int, toPosition: Int) {}

        override fun onChanged(position: Int, count: Int, payload: Any?) {}

    }

}