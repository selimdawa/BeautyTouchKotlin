package com.flatcode.beautytouchadmin.Auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private var binding: ActivityLoginBinding? = null
    var context: Context = this@LoginActivity
    private var auth: FirebaseAuth? = null
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        VOID.Intro(baseContext, binding!!.background, binding!!.backWhite, binding!!.backBlack)
        auth = FirebaseAuth.getInstance()

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)

        binding!!.forget.setOnClickListener { VOID.Intent1(context, CLASS.FORGET_PASSWORD) }
        binding!!.loginBtn.setOnClickListener { validateDate() }
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
            Toast.makeText(context, "Error entering the phone number!", Toast.LENGTH_SHORT).show()
        } else if (number.length != 10) {
            Toast.makeText(context, "Please enter a valid phone number!", Toast.LENGTH_SHORT).show()
        } else {
            val digit = number[0].toString().toInt()
            val digit2 = number[1].toString().toInt()
            val digit3 = number[2].toString().toInt()
            if (digit == 0 && digit2 == 9 && (digit3 == 3 || digit3 == 4 || digit3 == 5 || digit3 == 6 || digit3 == 8 || digit3 == 9)) {
                loginUser()
            } else {
                Toast.makeText(
                    context, "Please enter a valid phone number and password!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loginUser() {
        dialog!!.setMessage("Signed in...")
        dialog!!.show()
        try {
            auth!!.signInWithEmailAndPassword(email, password).addOnCanceledListener {
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
            }.addOnSuccessListener {
                VOID.IntentClear(context, CLASS.MAIN)
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener { dialog!!.show() }
        } catch (e: Exception) {
            dialog!!.dismiss()
            Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
        }
    }
}