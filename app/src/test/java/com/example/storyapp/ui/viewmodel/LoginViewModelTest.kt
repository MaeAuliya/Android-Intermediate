package com.example.storyapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.LoginResponse
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
import org.mockito.Mockito.*

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest{

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp(){
        loginViewModel = LoginViewModel(storyRepository)
    }

    @Test
    fun `when Login post Successfully and return value match with expected output`() = runTest {

        val dataDummy = DataDummy.generateLoginResponse()

        val expectedData = MutableLiveData<LoginResponse>()
        expectedData.value = dataDummy

        `when` (storyRepository.loginData).thenReturn(expectedData)

        val actualData = loginViewModel.loginData.getOrAwaitValue()
        advanceUntilIdle()

        verify(storyRepository).loginData

        assertNotNull(actualData)
        assertEquals(dataDummy, actualData)
    }


}