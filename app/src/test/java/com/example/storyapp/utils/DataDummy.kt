package com.example.storyapp.utils

import com.example.storyapp.data.model.StoryModel
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.LoginResult
import com.example.storyapp.data.response.RegisterResponse

object DataDummy {
    fun generateDummyStoryData() : List<StoryModel> {
        val storyList = ArrayList<StoryModel>()

        for (i in 0..10){
            val story = StoryModel(
                "story-FvU4u0Vp2S3PMsFg",
                "Tester",
                "Testing",
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "2023-05-11T20:02:32.900Z",
                -10.212,
                -16.002
            )
            storyList.add(story)
        }
        return storyList
    }

    fun generateRegisterResponse() : RegisterResponse = RegisterResponse(false, "success")

    fun generateLoginResponse() : LoginResponse {
        val user = LoginResult(
            "user-yj5pc_LARC_AgK61",
            "Maisan Auliya",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
        )
        return LoginResponse(
            false,
            "success",
            user
        )
    }

}