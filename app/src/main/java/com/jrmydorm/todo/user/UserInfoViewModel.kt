package com.jrmydorm.todo.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrmydorm.todo.models.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UserInfoViewModel : ViewModel() {
    private val userInfoRepository = UserInfoRepository()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    public val userInfo: StateFlow<UserInfo?> = _userInfo


    fun uploadAvatar(part: MultipartBody.Part) {
        viewModelScope.launch {
            val userInfoResponse = userInfoRepository.uploadAvatar(part)
            if(userInfoResponse != null){
                _userInfo.value = userInfoResponse
            }
        }
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            val userInfoResponse = userInfoRepository.loadUserInfo();
            if (userInfoResponse != null) {
                _userInfo.value = userInfoResponse
            }
        }
    }

    fun updateUserInfo(userInfo: UserInfo) {
        viewModelScope.launch {
            val userInfoResponse = userInfoRepository.updateUserInfo(userInfo)
            if (userInfoResponse != null) {
                _userInfo.value = userInfoResponse
            }
        }
    }
}