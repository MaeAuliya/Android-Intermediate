package com.example.storyapp.data.response

import com.example.storyapp.data.model.StoryModel
import com.google.gson.annotations.SerializedName


data class GetAllStoryResponse(
    @field: SerializedName("error")
    val error: Boolean,

    @field: SerializedName("message")
    val message: String,

    @field: SerializedName("listStory")
    val listStory: List<StoryModel>
)


