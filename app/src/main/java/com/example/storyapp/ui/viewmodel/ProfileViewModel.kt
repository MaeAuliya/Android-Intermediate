package com.example.storyapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.repository.StoryRepository
import com.example.storyapp.utils.UserPreferences
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getUser() : LiveData<UserModel> {
        return repository.getUser()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}