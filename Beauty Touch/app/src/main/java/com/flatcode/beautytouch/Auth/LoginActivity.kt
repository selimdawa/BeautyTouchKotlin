package com.flatcode.beautytouch.Auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.flatcode.beautytouch.Activity.MainActivity
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.Resource
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null
    var context: Context = this@LoginActivity
    private val viewModel: AuthViewModel by viewModels()
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)

        binding!!.forget.setOnClickListener { VOID.Intent1(context, ForgetPasswordActivity::class.java) }
        binding!!.noAccount.setOnClickListener { VOID.Intent1(context, RegisterActivity::class.java) }
        binding!!.loginBtn.setOnClickListener { validateDate() }

        observeViewModel()
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.loginState.collect { resource ->
                Timber.d("Login state collected: $resource")
                when (resource) {
                    is Resource.Loading -> {
                        dialog!!.setMessage("Signed in...")
                        dialog!!.show()
                    }

                    is Resource.Success -> {
                        dialog!!.dismiss()
                        VOID.IntentClear(context, MainActivity::class.java)
                    }

                    is Resource.Error -> {
                        dialog!!.dismiss()
                        Timber.e("Login error: ${resource.message}")
                        Toast.makeText(context, resource.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }
        }
    }

    private var number = "0111111111"
    private var email = ""
    private var password = ""
    private fun validateDate() {

        //get data
        email = binding!!.emailEt.text.toString().trim { it <= ' ' } + "@flatcodetest.com"
        number = binding!!.emailEt.text.toString().trim { it <= ' ' }
        password = binding!!.passwordEt.text.toString().trim { it <= ' ' }

        //validate data
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Password entry error!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(number)) {
            Toast.makeText(context, "Error entering the number!", Toast.LENGTH_SHORT).show()
        } else if (number.length != 10) {
            Toast.makeText(context, "Please enter a 10 digit number!", Toast.LENGTH_SHORT).show()
        } else if (number == "0111111111") {
            Toast.makeText(context, "Please enter a valid number!", Toast.LENGTH_SHORT).show()
        } else {
            val digit = number[0].toString().toInt()
            val digit2 = number[1].toString().toInt()
            val digit3 = number[2].toString().toInt()
            if (digit == 0 && digit2 == 9 && (digit3 == 3 || digit3 == 4 || digit3 == 5 || digit3 == 6 || digit3 == 8 || digit3 == 9)) {
                viewModel.login(email, password)
            } else {
                Toast.makeText(
                    context, "Please enter a valid phone number and password! ", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
