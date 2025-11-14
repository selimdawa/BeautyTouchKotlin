package com.flatcode.beautytouch.Auth

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouch.Unit.THEME
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.Unitimport.CLASS
import com.flatcode.beautytouch.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {

    private var binding: ActivityAuthBinding? = null
    var context: Context = this@AuthActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        VOID.Intro(baseContext, binding!!.background, binding!!.backWhite, binding!!.backBlack)

        binding!!.loginBtn.setOnClickListener { VOID.Intent1(context, CLASS.LOGIN) }
        binding!!.skipBtn.setOnClickListener { VOID.Intent1(context, CLASS.REGISTER) }
    }
}