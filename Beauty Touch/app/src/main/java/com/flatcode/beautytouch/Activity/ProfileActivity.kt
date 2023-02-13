package com.flatcode.beautytouch.Activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.flatcode.beautytouch.Model.User
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.THEME
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.databinding.ActivityProfileBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private var binding: ActivityProfileBinding? = null
    var activity: Activity? = null
    var context: Context = also { activity = it }
    private var imageUri: Uri? = null
    private var dialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        dialog = ProgressDialog(context)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)
        binding!!.back.setOnClickListener { onBackPressed() }
        binding!!.editImageIcon.setOnClickListener { VOID.CropImageSquare(activity) }
        binding!!.imageEdit.setOnClickListener { v ->
            binding!!.imageEdit.visibility = View.GONE
            binding!!.imageClose.visibility = View.VISIBLE
            binding!!.imageTrue.visibility = View.VISIBLE
            binding!!.name.visibility = View.GONE
            binding!!.nameEdit.visibility = View.VISIBLE
        }
        binding!!.imageClose.setOnClickListener {
            binding!!.imageEdit.visibility = View.VISIBLE
            binding!!.imageClose.visibility = View.GONE
            if (imageUri != null) {
                binding!!.imageTrue.visibility = View.VISIBLE
            } else {
                binding!!.imageTrue.visibility = View.GONE
            }
            binding!!.name.visibility = View.VISIBLE
            binding!!.nameEdit.visibility = View.GONE
        }
        binding!!.imageTrue.setOnClickListener {
            binding!!.imageClose.visibility = View.GONE
            binding!!.imageEdit.visibility = View.VISIBLE
            binding!!.imageTrue.visibility = View.GONE
            binding!!.name.visibility = View.VISIBLE
            binding!!.name.text = binding!!.nameEdit.text.toString()
            binding!!.nameEdit.visibility = View.GONE
            validateData()
        }
    }

    private var username = DATA.EMPTY
    private fun validateData() {
        username = binding!!.nameEdit.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(context, "Please enter the name", Toast.LENGTH_SHORT).show()
        } else {
            if (imageUri == null) {
                updateProfile(DATA.EMPTY)
            } else {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        dialog!!.setMessage("The image is loading...")
        dialog!!.show()
        val filePathAndName = "Images/Profile/" + DATA.FirebaseUserUid
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUrl = DATA.EMPTY + uriTask.result
                updateProfile(uploadedImageUrl)
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun updateProfile(imageUrl: String?) {
        dialog!!.setMessage("Modifications are loaded...")
        dialog!!.show()
        val hashMap = HashMap<String?, Any>()
        hashMap[DATA.USER_NAME] = DATA.EMPTY + username
        if (imageUri != null) {
            hashMap[DATA.IMAGE_URL] = DATA.EMPTY + imageUrl
        }
        val reference = FirebaseDatabase.getInstance().getReference(DATA.USERS)
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid)).updateChildren(hashMap)
            .addOnSuccessListener { unused: Void? ->
                dialog!!.dismiss()
                Toast.makeText(context, "Error loading image...", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun loadUserInfo() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.USERS)
        reference.child(Objects.requireNonNull(DATA.FirebaseUserUid))
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(
                        User::class.java
                    )!!
                    Glide.with(this@ProfileActivity).load(user.imageurl).into(binding!!.image)
                    binding!!.name.text = user.username
                    binding!!.nameEdit.setText(user.username)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri = CropImage.getPickImageResultUri(context, data)
            if (CropImage.isReadExternalStoragePermissionsRequired(context, uri)) {
                imageUri = uri
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            } else {
                VOID.CropImageSquare(activity)
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                imageUri = result.uri
                binding!!.image.setImageURI(imageUri)
                binding!!.imageTrue.visibility = View.VISIBLE
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "Something went wrong! $error", Toast.LENGTH_SHORT).show()
                binding!!.imageTrue.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        loadUserInfo()
        super.onResume()
    }

    override fun onRestart() {
        loadUserInfo()
        super.onRestart()
    }
}