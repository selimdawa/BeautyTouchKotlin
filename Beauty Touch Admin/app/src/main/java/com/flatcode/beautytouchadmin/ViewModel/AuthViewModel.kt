package com.flatcode.beautytouchadmin.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.beautytouchadmin.Repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private val _actionStatus = MutableSharedFlow<Result<String>>()
    val actionStatus: SharedFlow<Result<String>> = _actionStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun login(number: String, password: String) {
        val email = "$number@flatcodetest.com"
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.login(email, password)
                Timber.i("Login successful for user: $number")
                _actionStatus.emit(Result.success("Login successful"))
            } catch (e: Exception) {
                Timber.e(e, "Login failed for user: $number")
                _actionStatus.emit(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun forgetPassword(number: String) {
        val email = "$number@flatcodetest.com"
        _isLoading.value = true
        viewModelScope.launch {
            try {
                repository.sendPasswordReset(email)
                Timber.i("Password reset sent to: $email")
                _actionStatus.emit(Result.success("Password reset email sent to $email"))
            } catch (e: Exception) {
                Timber.e(e, "Failed to send password reset to: $email")
                _actionStatus.emit(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
