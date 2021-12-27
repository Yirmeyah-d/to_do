package com.jrmydorm.todo.data

import android.net.Uri
import com.jrmydorm.todo.models.UserInfo
import com.jrmydorm.todo.network.Api
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserInfoRepository {
    private val userWebService = Api.userWebService

    private fun convert(bytes: ByteArray): MultipartBody.Part {
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = bytes.toRequestBody()
        )
    }

    suspend fun uploadAvatar(bytes:ByteArray): UserInfo? {
        val updateAvatarResponse = userWebService.updateAvatar(convert(bytes))
        return if (updateAvatarResponse.isSuccessful) updateAvatarResponse.body() else null

    }


}