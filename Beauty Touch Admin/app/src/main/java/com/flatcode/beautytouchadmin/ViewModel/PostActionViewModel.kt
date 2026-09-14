package com.flatcode.beautytouchadmin.ViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.Repository.PostRepository
import com.flatcode.beautytouchadmin.Unit.DATA
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostActionViewModel @Inject constructor(private val repository: PostRepository) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post: StateFlow<Post?> = _post

    private val _actionStatus = MutableSharedFlow<Result<String>>()
    val actionStatus: SharedFlow<Result<String>> = _actionStatus

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun loadPost(postId: String) {
        viewModelScope.launch {
            repository.getPost(postId).collect {
                _post.value = it
            }
        }
    }

    fun addPost(
        name: String, indications: String, howToUse: String, price: String,
        category: String, imageUri: Uri, extension: String
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val id = repository.generatePostId() ?: throw Exception("Failed to generate ID")
                val imageUrl = repository.uploadImage(id, imageUri, extension)

                val hashMap = hashMapOf<String, Any?>(
                    "aname" to DATA.APP_NAME,
                    "category" to category,
                    "indications" to indications,
                    "name" to name,
                    "postid" to id,
                    "postimage" to imageUrl,
                    "postimage2" to DATA.EMPTY,
                    "postimage3" to DATA.EMPTY,
                    "postimage4" to DATA.EMPTY,
                    "postimage5" to DATA.EMPTY,
                    "postimage6" to DATA.EMPTY,
                    "postimage7" to DATA.EMPTY,
                    "postimage8" to DATA.EMPTY,
                    "postimage9" to DATA.EMPTY,
                    "postimage10" to DATA.EMPTY,
                    "timeStamp" to DATA.EMPTY + System.currentTimeMillis(),
                    "price" to price,
                    "publisher" to DATA.EMPTY + DATA.FirebaseUserUid,
                    "use" to howToUse
                )
                repository.addPost(hashMap)
                _actionStatus.emit(Result.success("Post added successfully"))
            } catch (e: Exception) {
                _actionStatus.emit(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updatePost(
        postId: String, name: String, indications: String, howToUse: String,
        price: String, category: String, imageUri: Uri?, extension: String?
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val hashMap = mutableMapOf<String, Any?>(
                    DATA.NAME to name,
                    "price" to price,
                    "indications" to indications,
                    "use" to howToUse,
                    "category" to category
                )

                if (imageUri != null && extension != null) {
                    val imageUrl = repository.uploadImage(postId, imageUri, extension)
                    hashMap["postimage"] = imageUrl
                }

                repository.updatePost(postId, hashMap)
                _actionStatus.emit(Result.success("Post updated successfully"))
            } catch (e: Exception) {
                _actionStatus.emit(Result.failure(e))
            } finally {
                _isLoading.value = false
            }
        }
    }
}
