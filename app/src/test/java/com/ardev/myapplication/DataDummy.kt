package com.ardev.myapplication

import com.ardev.myapplication.data.response.ListStoryItem
import com.ardev.myapplication.data.response.Story

object DataDummy {
    fun getDummyData(): List<ListStoryItem> {
        val newList = ArrayList<ListStoryItem>()
        for (i in 0..10) {
            val story = ListStoryItem(
                "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                "2022-01-08T06:34:18.598Z",
                "Dimas",
                "Lorem Ipsum",
                -16.002,
                "story-FvU4u0Vp2S3PMsFg",
                -10.212
            )
            newList.add(story)
        }
        return newList
    }
}