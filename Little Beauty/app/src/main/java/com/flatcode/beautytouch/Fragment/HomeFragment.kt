package com.flatcode.beautytouch.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.flatcode.beautytouch.Adapter.ImageSliderAdapter
import com.flatcode.beautytouch.Adapter.PostHotAdapter
import com.flatcode.beautytouch.Adapter.PostLinearAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.Resource
import com.flatcode.beautytouch.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    var hotpostAdapter: PostHotAdapter? = null
    var hotpostLists: MutableList<Post?>? = null
    var allpostAdapter: PostLinearAdapter? = null
    var allpostLists: MutableList<Post?>? = null
    var publisher = DATA.PUBLISHER_NAME
    var aname = DATA.APP_NAME

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        hotpostLists = ArrayList()
        hotpostAdapter = PostHotAdapter(context, hotpostLists as ArrayList<Post?>)
        binding!!.recyclerView.adapter = hotpostAdapter

        allpostLists = ArrayList()
        allpostAdapter = PostLinearAdapter(context, allpostLists as ArrayList<Post?>)
        binding!!.recyclerView2.adapter = allpostAdapter

        observeViewModel()
        return binding!!.root
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.sliderCount.collect { resource ->
                Timber.d("Slider count collected: $resource")
                if (resource is Resource.Success) {
                    binding!!.imageSlider.sliderAdapter = ImageSliderAdapter(context, resource.data)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.hotProducts.collect { resource ->
                Timber.d("Hot products collected: $resource")
                when (resource) {
                    is Resource.Loading -> {
                        binding!!.progressCircular.visibility = View.VISIBLE
                        binding!!.recyclerView.visibility = View.GONE
                    }

                    is Resource.Success -> {
                        hotpostLists!!.clear()
                        hotpostLists!!.addAll(resource.data)
                        hotpostAdapter!!.notifyDataSetChanged()
                        binding!!.progressCircular.visibility = View.GONE
                        binding!!.recyclerView.visibility = View.VISIBLE
                    }

                    is Resource.Error -> {
                        binding!!.progressCircular.visibility = View.GONE
                        Timber.e("Hot products error: ${resource.message}")
                    }

                    else -> {}
                }
            }
        }
        lifecycleScope.launch {
            viewModel.allPosts.collect { resource ->
                Timber.d("All posts collected: $resource")
                when (resource) {
                    is Resource.Loading -> {
                        binding!!.progressCircular2.visibility = View.VISIBLE
                        binding!!.recyclerView2.visibility = View.GONE
                    }

                    is Resource.Success -> {
                        allpostLists!!.clear()
                        allpostLists!!.addAll(resource.data)
                        allpostAdapter!!.notifyDataSetChanged()
                        binding!!.progressCircular2.visibility = View.GONE
                        binding!!.recyclerView2.visibility = View.VISIBLE
                    }

                    is Resource.Error -> {
                        binding!!.progressCircular2.visibility = View.GONE
                        Timber.e("All posts error: ${resource.message}")
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onResume() {
        viewModel.loadHomeData(publisher, aname)
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
