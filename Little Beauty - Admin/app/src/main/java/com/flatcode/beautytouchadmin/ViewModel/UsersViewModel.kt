package com.flatcode.beautytouchadmin.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.Repository.MainRepository
import com.flatcode.beautytouchadmin.Repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: UserRepository,
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    private val _usersCount = MutableStateFlow(0)
    val usersCount: StateFlow<Int> = _usersCount

    init {
        fetchUsers()
        fetchUsersCount()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            repository.getUsers().collect {
                _users.value = it
            }
        }
    }

    private fun fetchUsersCount() {
        viewModelScope.launch {
            mainRepository.getUsersCount().collect {
                _usersCount.value = it
            }
        }
    }
}
