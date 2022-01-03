package com.jrmydorm.todo.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jrmydorm.todo.data.UserInfoRepository
import com.jrmydorm.todo.models.UserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserInfoViewModel  : ViewModel() {
    private val userInfoRepository = UserInfoRepository()

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    public val userInfo : StateFlow<UserInfo?> = _userInfo

    fun uploadAvatar(readBytes: ByteArray) {
        viewModelScope.launch {
            _userInfo.value = userInfoRepository.uploadAvatar(readBytes);
        }
    }

    fun loadUserInfo(){
        viewModelScope.launch {
            val userResponse = userInfoRepository.loadUserInfo();
            if(userResponse != null){
                _userInfo.value = userResponse
            }
        }
    }
}