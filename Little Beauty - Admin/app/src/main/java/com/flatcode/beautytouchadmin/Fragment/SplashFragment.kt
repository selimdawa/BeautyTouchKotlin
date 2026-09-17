package com.flatcode.beautytouchadmin.Fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.ViewModel.SplashViewModel
import com.flatcode.beautytouchadmin.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var binding: ActivitySplashBinding? = null
    private val viewModel: SplashViewModel by viewModels()
    private val time_per_second = 2
    private val time_final = 1000 * time_per_second

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = ActivitySplashBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({ checkUser() }, time_final.toLong())
    }

    private fun checkUser() {
        if (!viewModel.isUserLoggedIn()) {
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        } else {
            findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
