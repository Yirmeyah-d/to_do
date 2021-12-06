package com.jrmydorm.todo.interfaces

import com.jrmydorm.todo.models.UserInfo
import retrofit2.Response
import retrofit2.http.GET

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>
}