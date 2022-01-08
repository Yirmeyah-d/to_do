package com.jrmydorm.todo.user

import com.jrmydorm.todo.models.UserInfo
import com.jrmydorm.todo.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserInfoRepository {
    private val userWebService = Api.userWebService

    suspend fun uploadAvatar(part : MultipartBody.Part): UserInfo? {
        val updateAvatarResponse = userWebService.updateAvatar(part)
        return if (updateAvatarResponse.isSuccessful) updateAvatarResponse.body() else null
    }

    suspend fun loadUserInfo(): UserInfo?{
        val getInfoResponse = Api.userWebService.getInfo()
        return if(getInfoResponse.isSuccessful) getInfoResponse.body() else null
    }

    suspend fun updateUserInfo(userInfo: UserInfo): UserInfo? {
        val response = userWebService.update(userInfo)
        return if (response.isSuccessful) response.body() else null
    }

}