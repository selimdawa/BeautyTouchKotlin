package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.beautytouchadmin.Adapter.MainAdapter
import com.flatcode.beautytouchadmin.Model.Main
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.ViewModel.MainState
import com.flatcode.beautytouchadmin.ViewModel.MainViewModel
import com.flatcode.beautytouchadmin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private val list = mutableListOf<Main>()
    private var adapter: MainAdapter? = null
    private val context: Context = this@MainActivity
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.toolbar.image.setOnClickListener {
            VOID.IntentExtra(context, CLASS.PROFILE, DATA.PROFILE_ID, DATA.FirebaseUserUid)
        }

        adapter = MainAdapter(context, list as ArrayList<Main>)
        binding!!.recyclerView.adapter = adapter

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
            VOID.Glide(true, context, user.imageurl, binding!!.toolbar.image)
        }

        list.clear()
        list.add(Main(R.drawable.ic_person_white, "Users", state.usersCount, CLASS.USERS))
        list.add(Main(R.drawable.ic_hot, "Hottest", state.hotProductsCount, CLASS.HOT_PRODUCTS))
        list.add(Main(R.drawable.ic_post, "My Posts", state.postsCount, CLASS.POSTS))
        list.add(Main(R.drawable.ic_add, "Add Post", 0, CLASS.POST_ADD))
        list.add(Main(R.drawable.ic_store, "Shopping Centers", state.shoppingCentersCount, CLASS.SHOPPING_CENTRES))
        list.add(Main(R.drawable.ic_add, "Add Shopping Center", 0, CLASS.SHOPPING_CENTRES_ADD))
        list.add(Main(R.drawable.ic_rank, "Current Session", 0, CLASS.SESSION_NOW))
        list.add(Main(R.drawable.ic_rank, "Previous Session", 0, CLASS.SESSION_OLD))
        list.add(Main(R.drawable.ic_slider, "Slider Show", state.sliderShowCount, CLASS.SLIDER_SHOW))
        list.add(Main(R.drawable.ic_ad, "Ad Monitor", 0, CLASS.ADS_METER))
        list.add(Main(R.drawable.ic_my, "About Me", 0, CLASS.ABOUT_ME))
        list.add(Main(R.drawable.ic_settings, "Tools", 0, CLASS.TOOLS))

        adapter?.notifyDataSetChanged()
        binding!!.progress.visibility = View.GONE
        binding!!.recyclerView.visibility = View.VISIBLE
    }
}
