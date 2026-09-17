package com.flatcode.beautytouch.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flatcode.beautytouch.Activity.PostViewModel
import com.flatcode.beautytouch.Adapter.ProductsStaggeredAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.Resource
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.databinding.FragmentSkinProductsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SkinProductsFragment : Fragment() {

    private var binding: FragmentSkinProductsBinding? = null
    private var list: MutableList<Post?>? = null
    private var adapter: ProductsStaggeredAdapter? = null
    var publisher = DATA.PUBLISHER_NAME
    var aname = DATA.APP_NAME

    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSkinProductsBinding.inflate(inflater, container, false)

        VOID.BannerAd(context, binding!!.adView, DATA.BANNER_SKIN)

        list = ArrayList()
        adapter = ProductsStaggeredAdapter(context, list as ArrayList<Post?>)
        binding!!.recyclerView.adapter = adapter

        observeViewModel()
        return binding!!.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.postsByCategory.collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        binding!!.bar.visibility = View.VISIBLE
                        binding!!.recyclerView.visibility = View.GONE
                        binding!!.emptyText.visibility = View.GONE
                    }

                    is Resource.Success -> {
                        list!!.clear()
                        list!!.addAll(resource.data)
                        list!!.reverse()
                        binding!!.bar.visibility = View.GONE
                        if (list!!.isNotEmpty()) {
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
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onResume() {
        viewModel.loadPostsByCategory(DATA.SKIN_PRODUCTS, publisher, aname)
        super.onResume()
    }
}
