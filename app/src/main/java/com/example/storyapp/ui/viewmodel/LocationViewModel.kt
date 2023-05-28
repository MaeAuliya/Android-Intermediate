package com.example.storyapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.network.ApiConfig
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.GetAllStoryResponse
import com.example.storyapp.utils.UserPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LocationViewModel(private val repository: StoryRepository) : ViewModel() {

    val allLocation : LiveData<GetAllStoryResponse> = repository.allLocation

    val isLoading : LiveData<Boolean> = repository.isLoading
    val isError : LiveData<Boolean> = repository.isError

    fun getAllLocation (token: String){
        viewModelScope.launch {
            repository.getAllLocation(token)
        }
    }

    fun getUser() : LiveData<UserModel> {
        return repository.getUser()
    }

}