package com.example.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.RegisterResponse
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.getOrAwaitValue
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
class RegisterViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp(){
        registerViewModel = RegisterViewModel(storyRepository)
    }

    @Test
    fun `when Register post successfully and match with expected output`() = runTest {

        val dataDummy = DataDummy.generateRegisterResponse()

        val expectedData = MutableLiveData<RegisterResponse>()
        expectedData.value = dataDummy

        `when` (storyRepository.registerData).thenReturn(expectedData)

        val actualData = registerViewModel.registerData.getOrAwaitValue()
        advanceUntilIdle()

        verify(storyRepository).registerData

        assertNotNull(actualData)
        assertEquals(dataDummy, actualData)
    }

}