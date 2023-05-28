package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.UploadStoryResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody


class UploadStoryViewModel (private val repository: StoryRepository) : ViewModel() {

    val uploadData : LiveData<UploadStoryResponse> = repository.uploadData

    val isLoading : LiveData<Boolean> = repository.isLoading
    val isError : LiveData<Boolean> = repository.isError

    fun postStory (token: String, image: MultipartBody.Part, description: RequestBody, lat: Double?= null, lon: Double?= null ){
        viewModelScope.launch {
            repository.postStory(token, image, description, lat, lon)
        }
    }

    fun getUser() : LiveData<UserModel> {
        return repository.getUser()
    }

}