package com.flatcode.beautytouch.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.flatcode.beautytouch.Adapter.PostDetailAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.R
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.Resource
import com.flatcode.beautytouch.databinding.ActivityPostDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailsActivity : AppCompatActivity() {

    var context: Context = this@PostDetailsActivity
    private var binding: ActivityPostDetailBinding? = null
    private var adapter: PostDetailAdapter? = null
    private var list: MutableList<Post?>? = null
    var postId: String? = null

    private val viewModel: PostDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val intent = intent
        postId = intent.getStringExtra(DATA.POST_ID)
        binding!!.toolbar.nameSpace.setText(R.string.post_detail)

        list = ArrayList()
        adapter = PostDetailAdapter(context, list)
        binding!!.recyclerView.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.postDetails.collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        list!!.clear()
                        list!!.add(resource.data)
                        adapter!!.notifyDataSetChanged()
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onResume() {
        if (postId != null) {
            viewModel.loadPostDetails(postId!!)
        }
        super.onResume()
    }
}
