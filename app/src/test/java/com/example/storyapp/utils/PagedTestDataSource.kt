package com.example.storyapp.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.data.model.StoryModel

class PagedTestDataSource : PagingSource<Int, LiveData<List<StoryModel>>>() {

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryModel>>>): Int = 0

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryModel>>> =
        LoadResult.Page(emptyList(), 0, 1)

    companion object {
        fun snapShot(stories: List<StoryModel>): PagingData<StoryModel> = PagingData.from(stories)
    }

}