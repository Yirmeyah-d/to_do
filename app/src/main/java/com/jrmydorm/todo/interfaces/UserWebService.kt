package com.jrmydorm.todo.interfaces

import android.net.Uri
import com.jrmydorm.todo.models.LoginForm
import com.jrmydorm.todo.models.LoginResponse
import com.jrmydorm.todo.models.UserInfo
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserWebService {
    @GET("users/info")
    suspend fun getInfo(): Response<UserInfo>

    @Multipart
    @PATCH("users/update_avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): Response<UserInfo>

    @PATCH("users")
    suspend fun update(@Body user: UserInfo): Response<UserInfo>

    @POST("users/login")
    suspend fun login(@Body user: LoginForm): Response<LoginResponse>
}