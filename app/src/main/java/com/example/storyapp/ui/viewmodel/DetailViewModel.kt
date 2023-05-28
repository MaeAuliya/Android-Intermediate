package com.example.storyapp.ui.viewmodel


import androidx.lifecycle.*
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.DetailStoryResponse
import kotlinx.coroutines.launch


class DetailViewModel (private val repository: StoryRepository) : ViewModel() {

    val detailData : LiveData<DetailStoryResponse> = repository.detailData

    val isLoading : LiveData<Boolean> = repository.isLoading
    val isError : LiveData<Boolean> = repository.isError

    fun getDetail (token: String, id : String){
        viewModelScope.launch {
            repository.getDetail(token, id)
        }
    }

    fun getUser() : LiveData<UserModel> {
        return repository.getUser()
    }

}