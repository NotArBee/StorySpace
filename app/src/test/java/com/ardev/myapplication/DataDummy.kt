package com.ardev.myapplication

import com.ardev.myapplication.data.database.StoryEntity
import com.ardev.myapplication.data.response.GeneralResponse
import com.ardev.myapplication.data.response.GetStoryResponse
import com.ardev.myapplication.data.response.GetStoryResult
import com.ardev.myapplication.data.response.LoginResponse
import com.ardev.myapplication.data.response.LoginResult

object DataDummy {
    fun generateDummyStoryResponse(): List<StoryEntity> {
        val item: MutableList<StoryEntity> = arrayListOf()
        for (i in 0..100) {
            val story = StoryEntity(
                i.toString(),
                "https://story-api.dicoding.dev/images/stories/photos-1671638971757_Vqh5nUSI.jpg",
                "2022-12-21T16:09:31.758Z",
                "nama $i",
                "desc $i",
                1.2,
                3.4
            )
            item.add(story)
        }
        return item
    }

    fun generateDummyLocationResponse(): GetStoryResponse {
        val item: MutableList<GetStoryResult> = arrayListOf()
        for (i in 0..100) {
            val story = GetStoryResult(

                "https://story-api.dicoding.dev/images/stories/photos-1671638971757_Vqh5nUSI.jpg",
                "2022-12-21T16:09:31.758Z",
                "nama $i",
                "desc $i",

                0.0,
                i.toString(),
                0.0
            )
            item.add(story)
        }
        return GetStoryResponse(
            item,false,"Stories fetched successfully"
        )
    }

    fun generateResponseRegister(): GeneralResponse {
        return GeneralResponse(false, "success"
        )
    }


    fun generateUploadSuccess(): GeneralResponse {
        return GeneralResponse(false, "success")
    }

    fun generateResponseLogin(): LoginResponse {
        val loginResult = LoginResult(
            "testingRZL",
            "user-q318hoJ5VoHbqLHa",
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXEzMThob0o1Vm9IYnFMSGEiLCJpYXQiOjE2NzE2Mzk3ODZ9.QkNIGzMjnnKKLSXVkmC0LlAVmfZvt0zAvcrnhY91p7o"
        )

        return LoginResponse(loginResult, false, "success")
    }
}