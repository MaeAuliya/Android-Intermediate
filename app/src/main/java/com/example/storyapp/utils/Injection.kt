package com.example.storyapp.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.network.ApiConfig
import com.example.storyapp.data.repository.StoryRepository


private val Context.datastore : DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideRepository(context: Context) : StoryRepository{
        val preferences = UserPreferences.getInstance(context.datastore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(preferences, apiService)
    }
}