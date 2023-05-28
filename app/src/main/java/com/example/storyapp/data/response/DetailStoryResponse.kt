package com.example.storyapp.data.response

import com.example.storyapp.data.model.StoryModel
import com.google.gson.annotations.SerializedName

data class DetailStoryResponse(
    @field: SerializedName("error")
    val error: Boolean,

    @field: SerializedName("message")
    val message: String,

    @field: SerializedName("story")
    val story: StoryModel? = null
)
