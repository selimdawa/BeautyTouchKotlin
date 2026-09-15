package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.ViewModel.SplashViewModel
import com.flatcode.beautytouchadmin.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private var binding: ActivitySplashBinding? = null
    private val context: Context = this@SplashActivity
    private val time_per_second = 2
    private val time_final = time_per_millis * time_per_second
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        Handler(Looper.getMainLooper()).postDelayed({ checkUser() }, time_final.toLong())
    }

    private fun checkUser() {
        if (!viewModel.isUserLoggedIn()) {
            VOID.Intent1(context, CLASS.LOGIN)
        } else {
            VOID.Intent1(context, CLASS.MAIN)
        }
        finish()
    }

    companion object {
        const val time_per_millis = 1000
    }
}
