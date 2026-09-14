package com.flatcode.beautytouch.Auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.beautytouch.Repository.AuthRepository
import com.flatcode.beautytouch.Unit.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<Boolean>?>(null)
    val loginState: StateFlow<Resource<Boolean>?> = _loginState

    private val _registerState = MutableStateFlow<Resource<Boolean>?>(null)
    val registerState: StateFlow<Resource<Boolean>?> = _registerState

    private val _forgetPasswordState = MutableStateFlow<Resource<String>?>(null)
    val forgetPasswordState: StateFlow<Resource<String>?> = _forgetPasswordState

    fun login(email: String, password: String) {
        Timber.d("Login attempt for email: $email")
        viewModelScope.launch {
            repository.login(email, password).collect {
                _loginState.value = it
                Timber.d("Login state updated: $it")
            }
        }
    }

    fun register(name: String, email: String, password: String, number: String) {
        Timber.d("Register attempt for email: $email, name: $name")
        viewModelScope.launch {
            repository.register(name, email, password, number).collect {
                _registerState.value = it
                Timber.d("Register state updated: $it")
            }
        }
    }

    fun forgetPassword(email: String) {
        Timber.d("Forget password requested for email: $email")
        viewModelScope.launch {
            repository.forgetPassword(email).collect {
                _forgetPasswordState.value = it
                Timber.d("Forget password state updated: $it")
            }
        }
    }
}