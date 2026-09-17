package com.flatcode.beautytouchadmin.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.flatcode.beautytouchadmin.adapter.LeaderboardAdapter
import com.flatcode.beautytouchadmin.model.User
import com.flatcode.beautytouchadmin.viewmodel.SessionViewModel
import com.flatcode.beautytouchadmin.databinding.ActivitySessionNowInfoBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SessionNowInfoActivity : AppCompatActivity() {

    private var binding: ActivitySessionNowInfoBinding? = null
    private val context: Context = this@SessionNowInfoActivity
    private val list = mutableListOf<User?>()
    private var adapter: LeaderboardAdapter? = null
    private val viewModel: SessionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionNowInfoBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.toolbar.nameSpace.text = "Session Now"
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        observeViewModel()
        viewModel.loadSessionInfo(false)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pointsKey.collect { key ->
                    if (key != null) {
                        adapter = LeaderboardAdapter(context, list, true, key)
                        binding!!.recyclerView.adapter = adapter
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.users.collect { users ->
                    list.clear()
                    list.addAll(users)
                    adapter?.notifyDataSetChanged()

                    binding!!.bar.visibility = View.GONE
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
        viewModel.loadSessionInfo(false)
    }
}
