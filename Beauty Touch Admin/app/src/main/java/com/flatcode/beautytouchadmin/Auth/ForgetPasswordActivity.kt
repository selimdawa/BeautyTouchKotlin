package com.flatcode.beautytouchadmin.Auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityForgetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordActivity : AppCompatActivity() {

    private var binding: ActivityForgetPasswordBinding? = null
    private val context: Context = this@ForgetPasswordActivity
    private var auth: FirebaseAuth? = null
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        VOID.Intro(baseContext, binding!!.background, binding!!.backWhite, binding!!.backBlack)
        auth = FirebaseAuth.getInstance()

        dialog = ProgressDialog(this)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)

        binding!!.login.setOnClickListener {
            VOID.Intent1(context, CLASS.LOGIN)
            finish()
        }
        binding!!.go.setOnClickListener { validateDate() }
    }

    private var number = "0111111111"
    private var email = ""
    private fun validateDate() {

        //get data
        email = binding!!.emailEt.text.toString().trim { it <= ' ' } + "@flatcodetest.com"
        number = binding!!.emailEt.text.toString().trim { it <= ' ' }
        if (email.isEmpty()) {
            Toast.makeText(context, "Enter the number!", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.PHONE.matcher(email).matches()) {
            Toast.makeText(context, "Phone number is incorrect!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(number)) {
            Toast.makeText(context, "Error entering the phone number!", Toast.LENGTH_SHORT).show()
        } else if (number.length != 10) {
            Toast.makeText(context, "Please enter a valid phone number!", Toast.LENGTH_SHORT).show()
        } else {
            val digit = number[0].toString().toInt()
            val digit2 = number[1].toString().toInt()
            val digit3 = number[2].toString().toInt()
            if (digit == 0 && digit2 == 9 && (digit3 == 3 || digit3 == 4 || digit3 == 5 || digit3 == 6 || digit3 == 8 || digit3 == 9)) {
                recoverPassword()
            } else {
                Toast.makeText(
                    context, "Please enter a valid phone number and password!", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun recoverPassword() {
        dialog!!.setMessage("Password recovery is sent to $email")
        dialog!!.show()
        auth!!.sendPasswordResetEmail(email).addOnCompleteListener {
            dialog!!.dismiss()
            Toast.makeText(context, "Password reset has been sent to $email", Toast.LENGTH_SHORT)
                .show()
        }.addOnFailureListener { e: Exception ->
            dialog!!.dismiss()
            Toast.makeText(context, "Something went wrong!" + e.message, Toast.LENGTH_SHORT).show()
        }
    }
}