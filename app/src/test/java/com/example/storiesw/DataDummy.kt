package com.example.storiesw

import com.example.storiesw.data.model.Story


object DataDummy {

    fun generateDummyStory(): List<Story> {
        val items = arrayListOf<Story>()
        for(i in 0 until 5) {
            val story = Story(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "Dimas",
                description = "Lorem Ipsum",
                latitude = -10.212f,
                longitude = -16.002f
            )
            items.add(story)
        }
        return items
    }
}