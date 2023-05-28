package com.example.storyapp.ui.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.RegisterResponse
import kotlinx.coroutines.launch


class RegisterViewModel(private val repository: StoryRepository) : ViewModel() {

    val registerData : LiveData<RegisterResponse> by lazy {
        repository.registerData
    }

    val isLoading : LiveData<Boolean> = repository.isLoading
    val isError : LiveData<Boolean> = repository.isError

    fun postRegister (name: String, email: String, password: String){
        viewModelScope.launch {
            repository.postRegister(name, email, password)
        }
    }

}