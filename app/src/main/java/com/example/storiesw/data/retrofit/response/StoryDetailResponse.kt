package com.example.storiesw.data.retrofit.response

import com.example.storiesw.data.model.Story
import com.google.gson.annotations.SerializedName

data class StoryDetailResponse(
    val error: Boolean,
    val message: String,
    @SerializedName("story")
    val data: Story
)
