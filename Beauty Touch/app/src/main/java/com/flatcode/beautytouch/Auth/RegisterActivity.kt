package com.flatcode.beautytouch.Auth

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.THEME
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.Unitimport.CLASS
import com.flatcode.beautytouch.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private var binding: ActivityRegisterBinding? = null
    var context: Context = this@RegisterActivity
    private var auth: FirebaseAuth? = null
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
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
        binding!!.forget.setOnClickListener { VOID.Intent1(context, CLASS.FORGET_PASSWORD) }
        binding!!.go.setOnClickListener { validateData() }
    }

    private var name = ""
    private var email = ""
    private var password = ""
    private var number = "0111111111"
    private fun validateData() {

        //get data
        name = binding!!.nameEt.text.toString().trim { it <= ' ' }
        number = binding!!.emailEt.text.toString().trim { it <= ' ' }
        email = binding!!.emailEt.text.toString().trim { it <= ' ' } + "@flatcodetest.com"
        password = binding!!.passwordEt.text.toString().trim { it <= ' ' }
        val cPassword = binding!!.cPasswordEt.text.toString().trim { it <= ' ' }

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter the username!", Toast.LENGTH_SHORT).show()
        } else if (!Patterns.PHONE.matcher(number).matches()) {
            Toast.makeText(context, "Enter the Phone number!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "Enter the password!", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(cPassword)) {
            Toast.makeText(context, "Confirm password!", Toast.LENGTH_SHORT).show()
        } else if (password != cPassword) {
            Toast.makeText(context, "Password does not match!", Toast.LENGTH_SHORT).show()
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
                createUserAccount()
            } else {
                Toast.makeText(context, "Please enter valid data!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createUserAccount() {
        //show progress
        dialog!!.setMessage("The account is created")
        dialog!!.show()

        //create user in firebase auth
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { updateUserinfo() }
            .addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserinfo() {
        dialog!!.setMessage("User data is saved")
        //get current user uid, since user is registered so we can get now
        val id = auth!!.uid

        //setup data to add in db
        val hashMap = HashMap<String?, Any?>()
        hashMap[DATA.ID] = id
        hashMap["started"] = "" + System.currentTimeMillis()
        hashMap["phonenumber"] = number
        hashMap["password"] = password
        hashMap["mversion"] = DATA.CURRENT_VERSION
        hashMap["imageurl"] = DATA.BASIC
        hashMap["username"] = name

        //set data to db
        val ref = FirebaseDatabase.getInstance().getReference(DATA.USERS)
        assert(id != null)
        ref.child(id!!).setValue(hashMap).addOnSuccessListener {
            //data added to db
            dialog!!.dismiss()
            Toast.makeText(context, "Account created", Toast.LENGTH_SHORT).show()
            VOID.IntentClear(context, CLASS.MAIN)
            finish()
        }.addOnFailureListener { e: Exception ->
            //data failed adding to db
            Toast.makeText(context, DATA.EMPTY + e.message, Toast.LENGTH_SHORT).show()
        }
    }
}