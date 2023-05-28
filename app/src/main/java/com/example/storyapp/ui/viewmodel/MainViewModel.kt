package com.example.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.network.ApiConfig
import com.example.storyapp.data.network.ApiService
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.GetAllStoryResponse
import com.example.storyapp.utils.StoryPagingSource
import com.example.storyapp.utils.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel (private val repository: StoryRepository) : ViewModel() {

    fun listStories(): LiveData<PagingData<StoryModel>> = repository.loadStories().cachedIn(viewModelScope)

    fun getUser() : LiveData<UserModel> {
        return repository.getUser()
    }

}