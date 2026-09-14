package com.flatcode.beautytouch.Fragment

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
class HomeViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _hotProducts = MutableStateFlow<Resource<List<Post>>?>(null)
    val hotProducts: StateFlow<Resource<List<Post>>?> = _hotProducts

    private val _allPosts = MutableStateFlow<Resource<List<Post>>?>(null)
    val allPosts: StateFlow<Resource<List<Post>>?> = _allPosts

    private val _sliderCount = MutableStateFlow<Resource<Int>?>(null)
    val sliderCount: StateFlow<Resource<Int>?> = _sliderCount

    fun loadHomeData(publisher: String, aname: String) {
        viewModelScope.launch {
            repository.getHotProducts(publisher, aname).collect {
                _hotProducts.value = it
            }
        }
        viewModelScope.launch {
            repository.getAllPosts(publisher, aname).collect {
                _allPosts.value = it
            }
        }
        viewModelScope.launch {
            repository.getImageSliderCount().collect {
                _sliderCount.value = it
            }
        }
    }
}