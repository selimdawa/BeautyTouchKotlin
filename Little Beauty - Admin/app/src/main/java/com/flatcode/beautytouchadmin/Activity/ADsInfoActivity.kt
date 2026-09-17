package com.flatcode.beautytouchadmin.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.beautytouchadmin.adapter.ADsInfoAdapter
import com.flatcode.beautytouchadmin.model.ADs
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.unit.DATA
import com.flatcode.beautytouchadmin.unit.VOID
import com.flatcode.beautytouchadmin.viewmodel.ADsViewModel
import com.flatcode.beautytouchadmin.databinding.ActivityAdsInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ADsInfoActivity : AppCompatActivity() {

    private var binding: ActivityAdsInfoBinding? = null
    private val context: Context = this@ADsInfoActivity
    private val list = mutableListOf<ADs?>()
    private var adapter: ADsInfoAdapter? = null
    private var profileId: String? = null
    private val viewModel: ADsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdsInfoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        profileId = intent.getStringExtra(DATA.PROFILE_ID)

        binding!!.toolbar.nameSpace.setText(R.string.info_ads)
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        adapter = ADsInfoAdapter(context, list, true)
        binding!!.recyclerView.adapter = adapter

        profileId?.let {
            viewModel.fetchUserInfo(it)
            viewModel.fetchAds(it, DATA.NAME)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.user.collect { user ->
                    user?.let {
                        binding!!.username.text = it.username
                        VOID.Glide(true, context, it.imageurl, binding!!.profileImage)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.ads.collect { ads ->
                    list.clear()
                    list.addAll(ads)
                    adapter?.notifyDataSetChanged()

                    binding!!.progress.visibility = View.GONE
                    if (list.isNotEmpty()) {
                        binding!!.recyclerView.visibility = View.VISIBLE
                        binding!!.emptyText.visibility = View.GONE
                    } else {
                        binding!!.recyclerView.visibility = View.GONE
                        binding!!.emptyText.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        profileId?.let { viewModel.fetchAds(it, DATA.NAME) }
    }
}
