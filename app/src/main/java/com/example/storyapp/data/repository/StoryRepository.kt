package com.example.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.data.model.UserModel
import com.example.storyapp.data.network.ApiConfig
import com.example.storyapp.data.network.ApiService
import com.example.storyapp.data.response.*
import com.example.storyapp.utils.StoryPagingSource
import com.example.storyapp.utils.UserPreferences
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository private constructor(
    private val preferences: UserPreferences,
    private val apiService: ApiService
){
    private val _registerData = MutableLiveData<RegisterResponse>()
    val registerData : LiveData<RegisterResponse> = _registerData

    private val _loginData = MutableLiveData<LoginResponse>()
    val loginData : LiveData<LoginResponse> = _loginData

    private val _detailData = MutableLiveData<DetailStoryResponse>()
    val detailData : LiveData<DetailStoryResponse> = _detailData

    private val _allLocation = MutableLiveData<GetAllStoryResponse>()
    val allLocation : LiveData<GetAllStoryResponse> = _allLocation

    private val _uploadData = MutableLiveData<UploadStoryResponse>()
    val uploadData : LiveData<UploadStoryResponse> = _uploadData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError : LiveData<Boolean> = _isError

    fun postRegister (name: String, email: String, password: String){
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().postRegister(name, email, password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                _isLoading.value = false
                _isError.value = false
                if (response.isSuccessful && response.body() != null){
                    _registerData.value = response.body()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")

            }

        })
    }

    fun postLogin (email: String, password: String){
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().postLogin(email, password)
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _isLoading.value = false
                _isError.value = false
                if (response.isSuccessful && response.body() != null){
                    _loginData.value = response.body()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun loadStories() : LiveData<PagingData<StoryModel>>{
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, preferences)
            }
        ).liveData
    }

    fun getDetail (token: String, id : String){
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().getDetailStory(token, id)
        client.enqueue(object : Callback<DetailStoryResponse>{
            override fun onResponse(
                call: Call<DetailStoryResponse>,
                response: Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                _isError.value = false
                if (response.isSuccessful && response.body() != null){
                    _detailData.value = response.body()
                }
            }

            override fun onFailure(call: Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getAllLocation (token: String){
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().getAllStoryLocation(token)
        client.enqueue(object : Callback<GetAllStoryResponse>{
            override fun onResponse(
                call: Call<GetAllStoryResponse>,
                response: Response<GetAllStoryResponse>
            ) {
                _isLoading.value = false
                _isError.value = false
                if (response.isSuccessful && response.body() != null){
                    _allLocation.value = response.body()
                }
            }

            override fun onFailure(call: Call<GetAllStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun postStory (token: String, image: MultipartBody.Part, description: RequestBody, lat: Double?= null, lon: Double?= null ){
        _isLoading.value = true
        _isError.value = false
        val client = ApiConfig.getApiService().postStory(token, image, description, lat, lon)
        client.enqueue(object : Callback<UploadStoryResponse>{
            override fun onResponse(
                call: Call<UploadStoryResponse>,
                response: Response<UploadStoryResponse>
            ) {
                _isLoading.value = false
                _isError.value = false
                if (response.isSuccessful && response.body() != null){
                    _uploadData.value = response.body()
                }
            }

            override fun onFailure(call: Call<UploadStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }

        })
    }

    fun getUser() : LiveData<UserModel> {
        return preferences.getUser().asLiveData()
    }

    suspend fun saveUser(user: UserModel){
        preferences.saveUser(user)
    }

    suspend fun logout() {
        preferences.logout()
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var INSTANCE: StoryRepository? = null

        fun getInstance(
            preferences: UserPreferences,
            apiService: ApiService
        ): StoryRepository =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: StoryRepository(preferences, apiService)
            }.also { INSTANCE = it }
    }


}