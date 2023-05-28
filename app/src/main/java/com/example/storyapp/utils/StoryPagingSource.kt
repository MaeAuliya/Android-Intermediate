package com.example.storyapp.utils

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.data.network.ApiService
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val apiService: ApiService, private val preferences: UserPreferences) : PagingSource<Int, StoryModel>() {
    override fun getRefreshKey(state: PagingState<Int, StoryModel>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryModel> {
        return try {
            val token = preferences.getUser().first().token
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStories(token, position, params.loadSize)

            if (responseData.isSuccessful){
                Log.d("StoryPagingSource", "Load: ${responseData.body()}")
                    LoadResult.Page(
                        data = responseData.body()?.listStory ?: emptyList(),
                        prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                        nextKey = if (responseData.body()?.listStory.isNullOrEmpty()) null else position + 1
                    )
            } else {
                Log.d("Token", "Error: $token")
                LoadResult.Error(Exception("Failed"))
            }
        } catch (e: Exception) {
            Log.d("Exception", "Error: ${e.message}")
            return LoadResult.Error(e)
        }
    }


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}