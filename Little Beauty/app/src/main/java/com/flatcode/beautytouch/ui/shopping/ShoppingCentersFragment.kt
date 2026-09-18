package com.flatcode.beautytouch.ui.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flatcode.beautytouch.ui.post.PostViewModel
import com.flatcode.beautytouch.ui.adapter.ShoppingCentersAdapter
import com.flatcode.beautytouch.model.ShoppingCenter
import com.flatcode.beautytouch.utils.DATA
import com.flatcode.beautytouch.utils.Resource
import com.flatcode.beautytouch.utils.VOID
import com.flatcode.beautytouch.databinding.FragmentShoppingCentersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShoppingCentersFragment : Fragment() {

    private var binding: FragmentShoppingCentersBinding? = null
    private var list: MutableList<ShoppingCenter?>? = null
    private var adapter: ShoppingCentersAdapter? = null
    var publisher = DATA.PUBLISHER_NAME
    var aname = DATA.APP_NAME

    private val viewModel: PostViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShoppingCentersBinding.inflate(inflater, container, false)

        VOID.BannerAd(context, binding!!.adView, DATA.BANNER_SHOPPING_CENTRES)

        list = ArrayList()
        adapter = ShoppingCentersAdapter(context, list as ArrayList<ShoppingCenter?>)
        binding!!.recyclerView.adapter = adapter

        observeViewModel()
        return binding!!.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.shoppingCenters.collect { resource ->
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
        viewModel.loadShoppingCenters(publisher, aname)
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}