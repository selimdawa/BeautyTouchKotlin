package com.flatcode.beautytouch.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.flatcode.beautytouch.Adapter.ProductsStaggeredAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.R
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.Resource
import com.flatcode.beautytouch.Unit.VOID.BannerAd
import com.flatcode.beautytouch.databinding.ActivityFavoritesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class FavoritesActivity : AppCompatActivity() {

    private val context: Context = this@FavoritesActivity
    private var binding: ActivityFavoritesBinding? = null
    private var postList: MutableList<Post?>? = null
    private var adapter: ProductsStaggeredAdapter? = null
    var publisher = DATA.PUBLISHER_NAME
    var aname = DATA.APP_NAME

    private val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.favorites)
        BannerAd(applicationContext, binding!!.adView, DATA.BANNER_FAVORITES)

        postList = ArrayList()
        adapter = ProductsStaggeredAdapter(context, postList as ArrayList<Post?>)
        binding!!.recyclerView.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.favoritePosts.collect { resource ->
                Timber.d("Favorite posts collected: $resource")
                when (resource) {
                    is Resource.Loading -> {
                        binding!!.bar.visibility = View.VISIBLE
                        binding!!.recyclerView.visibility = View.GONE
                        binding!!.emptyText.visibility = View.GONE
                    }
                    is Resource.Success -> {
                        postList!!.clear()
                        postList!!.addAll(resource.data)
                        postList!!.reverse()
                        binding!!.bar.visibility = View.GONE
                        if (postList!!.isNotEmpty()) {
                            binding!!.recyclerView.visibility = View.VISIBLE
                            binding!!.emptyText.visibility = View.GONE
                        } else {
                            binding!!.recyclerView.visibility = View.GONE
                            binding!!.emptyText.visibility = View.VISIBLE
                        }
                        adapter!!.notifyDataSetChanged()
                    }
                    is Resource.Error -> {
                        binding!!.bar.visibility = View.GONE
                        Timber.e("Favorite posts error: ${resource.message}")
                    }
                    else -> {}
                }
            }
        }
    }

    override fun onResume() {
        viewModel.loadFavoritePosts(publisher, aname)
        super.onResume()
    }
}
