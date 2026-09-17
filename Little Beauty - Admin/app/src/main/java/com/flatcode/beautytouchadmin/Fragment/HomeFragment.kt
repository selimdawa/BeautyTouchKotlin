package com.flatcode.beautytouchadmin.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.beautytouchadmin.adapter.MainAdapter
import com.flatcode.beautytouchadmin.model.Main
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.unit.DATA
import com.flatcode.beautytouchadmin.unit.VOID
import com.flatcode.beautytouchadmin.viewmodel.MainState
import com.flatcode.beautytouchadmin.viewmodel.MainViewModel
import com.flatcode.beautytouchadmin.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private val list = mutableListOf<Main>()
    private var adapter: MainAdapter? = null
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding!!.toolbar.image.setOnClickListener {
            // TODO: Use NavController to navigate to Profile
        }

        adapter = MainAdapter(requireContext(), list as ArrayList<Main>)
        binding!!.recyclerView.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (!state.isLoading) {
                        updateUI(state)
                    }
                }
            }
        }
    }

    private fun updateUI(state: MainState) {
        state.user?.let { user ->
            VOID.Glide(true, requireContext(), user.imageurl, binding!!.toolbar.image)
        }

        list.clear()
        list.add(Main(R.drawable.ic_person_white, "Users", state.usersCount))
        list.add(Main(R.drawable.ic_hot, "Hottest", state.hotProductsCount))
        list.add(Main(R.drawable.ic_post, "My Posts", state.postsCount))
        list.add(Main(R.drawable.ic_add, "Add Post", 0))
        list.add(Main(R.drawable.ic_store, "Shopping Centers", state.shoppingCentersCount))
        list.add(Main(R.drawable.ic_add, "Add Shopping Center", 0))
        list.add(Main(R.drawable.ic_rank, "Current Session", 0))
        list.add(Main(R.drawable.ic_rank, "Previous Session", 0))
        list.add(Main(R.drawable.ic_slider, "Slider Show", state.sliderShowCount))
        list.add(Main(R.drawable.ic_ad, "Ad Monitor", 0))
        list.add(Main(R.drawable.ic_my, "About Me", 0))
        list.add(Main(R.drawable.ic_settings, "Tools", 0))

        adapter?.notifyDataSetChanged()
        binding!!.progress.visibility = View.GONE
        binding!!.recyclerView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
