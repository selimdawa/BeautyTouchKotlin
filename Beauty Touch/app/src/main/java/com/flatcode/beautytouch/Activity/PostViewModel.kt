package com.flatcode.beautytouch.Activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Model.ShoppingCenter
import com.flatcode.beautytouch.Repository.PostRepository
import com.flatcode.beautytouch.Unit.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _postsByCategory = MutableStateFlow<Resource<List<Post>>?>(null)
    val postsByCategory: StateFlow<Resource<List<Post>>?> = _postsByCategory

    private val _skinCount = MutableStateFlow<Resource<Int>?>(null)
    val skinCount: StateFlow<Resource<Int>?> = _skinCount

    private val _hairCount = MutableStateFlow<Resource<Int>?>(null)
    val hairCount: StateFlow<Resource<Int>?> = _hairCount

    private val _shoppingCount = MutableStateFlow<Resource<Int>?>(null)
    val shoppingCount: StateFlow<Resource<Int>?> = _shoppingCount

    private val _shoppingCenters = MutableStateFlow<Resource<List<ShoppingCenter>>?>(null)
    val shoppingCenters: StateFlow<Resource<List<ShoppingCenter>>?> = _shoppingCenters

    private val _favoritePosts = MutableStateFlow<Resource<List<Post>>?>(null)
    val favoritePosts: StateFlow<Resource<List<Post>>?> = _favoritePosts

    fun loadPostsByCategory(category: String, publisher: String, aname: String) {
        viewModelScope.launch {
            repository.getPostsByCategory(category, publisher, aname).collect {
                _postsByCategory.value = it
            }
        }
    }

    fun loadCategoryCounts(publisher: String, aname: String, skinCat: String, hairCat: String, shoppingCat: String) {
        viewModelScope.launch {
            repository.getCategoryCount(skinCat, publisher, aname).collect {
                _skinCount.value = it
            }
        }
        viewModelScope.launch {
            repository.getCategoryCount(hairCat, publisher, aname).collect {
                _hairCount.value = it
            }
        }
        viewModelScope.launch {
            repository.getCategoryCount(shoppingCat, publisher, aname).collect {
                _shoppingCount.value = it
            }
        }
    }

    fun loadShoppingCenters(publisher: String, aname: String) {
        viewModelScope.launch {
            repository.getShoppingCenters(publisher, aname).collect {
                _shoppingCenters.value = it
            }
        }
    }

    fun loadFavoritePosts(publisher: String, aname: String) {
        viewModelScope.launch {
            repository.getFavoritePosts(publisher, aname).collect {
                _favoritePosts.value = it
            }
        }
    }
}