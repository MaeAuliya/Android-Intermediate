package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.*
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.data.response.LoginResponse
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: StoryRepository) : ViewModel() {

    val loginData : LiveData<LoginResponse> by lazy {
        repository.loginData
    }

    val isLoading : LiveData<Boolean> = repository.isLoading
    val isError : LiveData<Boolean> = repository.isError

    fun postLogin (email: String, password: String){
        viewModelScope.launch {
            repository.postLogin(email, password)
        }
    }

    fun saveUser(user: UserModel){
        viewModelScope.launch {
            repository.saveUser(user)
        }
    }

}