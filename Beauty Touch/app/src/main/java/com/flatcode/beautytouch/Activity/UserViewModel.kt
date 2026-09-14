package com.flatcode.beautytouch.Activity

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.beautytouch.Model.Tools
import com.flatcode.beautytouch.Model.User
import com.flatcode.beautytouch.Model.Reward
import com.flatcode.beautytouch.Repository.UserRepository
import com.flatcode.beautytouch.Unit.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _userInfo = MutableStateFlow<Resource<User>?>(null)
    val userInfo: StateFlow<Resource<User>?> = _userInfo

    private val _appTools = MutableStateFlow<Resource<Tools>?>(null)
    val appTools: StateFlow<Resource<Tools>?> = _appTools

    private val _updateProfileState = MutableStateFlow<Resource<Boolean>?>(null)
    val updateProfileState: StateFlow<Resource<Boolean>?> = _updateProfileState

    private val _uploadImageState = MutableStateFlow<Resource<String>?>(null)
    val uploadImageState: StateFlow<Resource<String>?> = _uploadImageState

    private val _points = MutableStateFlow<Resource<String>?>(null)
    val points: StateFlow<Resource<String>?> = _points

    private val _leaderboard = MutableStateFlow<Resource<List<User>>?>(null)
    val leaderboard: StateFlow<Resource<List<User>>?> = _leaderboard

    private val _rewards = MutableStateFlow<Resource<Reward>?>(null)
    val rewards: StateFlow<Resource<Reward>?> = _rewards

    fun loadUserInfo() {
        viewModelScope.launch {
            repository.getUserInfo().collect {
                _userInfo.value = it
            }
        }
    }

    fun loadAppTools() {
        viewModelScope.launch {
            repository.getAppTools().collect {
                _appTools.value = it
            }
        }
    }

    fun logout() {
        repository.logout()
    }

    fun updateProfile(username: String, imageUrl: String? = null) {
        viewModelScope.launch {
            repository.updateProfile(username, imageUrl).collect {
                _updateProfileState.value = it
            }
        }
    }

    fun uploadProfileImage(uri: Uri, extension: String) {
        viewModelScope.launch {
            repository.uploadProfileImage(uri, extension).collect {
                _uploadImageState.value = it
            }
        }
    }

    fun loadPoints(year: String, session: String) {
        viewModelScope.launch {
            repository.getPoints(year, session).collect {
                _points.value = it
            }
        }
    }

    fun addRewardPoint(year: String, session: String) {
        repository.addRewardPoint(year, session)
    }

    fun loadLeaderboard(orderBy: String, limit: Int = 3) {
        viewModelScope.launch {
            repository.getLeaderboard(orderBy, limit).collect {
                _leaderboard.value = it
            }
        }
    }

    fun loadRewards() {
        viewModelScope.launch {
            repository.getRewards().collect {
                _rewards.value = it
            }
        }
    }
}