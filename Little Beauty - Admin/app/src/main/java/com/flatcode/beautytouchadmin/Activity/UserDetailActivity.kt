package com.flatcode.beautytouchadmin.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.beautytouchadmin.adapter.FavoritesAdapter
import com.flatcode.beautytouchadmin.model.Post
import com.flatcode.beautytouchadmin.unit.DATA
import com.flatcode.beautytouchadmin.unit.VOID
import com.flatcode.beautytouchadmin.viewmodel.UserDetailViewModel
import com.flatcode.beautytouchadmin.databinding.ActivityUserDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {

    private var binding: ActivityUserDetailBinding? = null
    private val context: Context = this@UserDetailActivity
    private val list = mutableListOf<Post?>()
    private var adapter: FavoritesAdapter? = null
    private var profileId: String? = null
    private val viewModel: UserDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        profileId = intent.getStringExtra(DATA.PROFILE_ID)

        adapter = FavoritesAdapter(context, list)
        binding!!.recyclerView.adapter = adapter

        profileId?.let { viewModel.loadData(it) }
        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    user?.let {
                        VOID.Glide(true, context, it.imageurl, binding!!.image)
                        binding!!.name.text = it.username
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.savedPosts.collect { posts ->
                    list.clear()
                    list.addAll(posts)
                    adapter?.notifyDataSetChanged()
                    binding!!.recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        profileId?.let { viewModel.loadData(it) }
    }
}
