package com.flatcode.beautytouch.Activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Repository.PostRepository
import com.flatcode.beautytouch.Unit.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostDetailsViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _postDetails = MutableStateFlow<Resource<Post>?>(null)
    val postDetails: StateFlow<Resource<Post>?> = _postDetails

    fun loadPostDetails(postId: String) {
        viewModelScope.launch {
            repository.getPostDetails(postId).collect {
                _postDetails.value = it
            }
        }
        repository.addPostView(postId)
    }
}