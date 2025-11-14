package com.flatcode.beautytouchadmin.Activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityPostAddBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage

class PostAddActivity : AppCompatActivity() {

    private var binding: ActivityPostAddBinding? = null
    var activity: Activity? = null
    var context: Context = also { activity = it }
    private var imageUri: Uri? = null
    private var dialog: ProgressDialog? = null
    var typePost = DATA.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityPostAddBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        dialog = ProgressDialog(context)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)

        binding!!.toolbar.nameSpace.setText(R.string.add_post)
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        binding!!.typeOne.setOnClickListener {
            typePost = "Skin Products"
            binding!!.typeOne.text = "Skin Products ✓"
            binding!!.typeTwo.text = "Hair Products"
        }
        binding!!.typeTwo.setOnClickListener {
            typePost = "Hair Products"
            binding!!.typeOne.text = "Skin Products"
            binding!!.typeTwo.text = "Hair Products ✓"
        }
        binding!!.go.setOnClickListener { validateData() }
        binding!!.layoutImageProfile.setOnClickListener { VOID.CropImageSquare(activity) }
    }

    private var name = DATA.EMPTY
    private var indications = DATA.EMPTY
    private var howToUse = DATA.EMPTY
    private var price = DATA.EMPTY
    private fun validateData() {
        //get data
        name = binding!!.name.text.toString().trim { it <= ' ' }
        indications = binding!!.indications.text.toString().trim { it <= ' ' }
        howToUse = binding!!.howToUse.text.toString().trim { it <= ' ' }
        price = binding!!.price.text.toString().trim { it <= ' ' }

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter a name", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(indications)) {
            Toast.makeText(context, "Enter the indications", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(howToUse)) {
            Toast.makeText(context, "Enter how to use", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(price)) {
            Toast.makeText(context, "Enter a price", Toast.LENGTH_SHORT).show()
        } else if (typePost == DATA.EMPTY) {
            Toast.makeText(context, "Enter a product category", Toast.LENGTH_SHORT).show()
        } else if (imageUri == null) {
            Toast.makeText(context, "There's no picture!", Toast.LENGTH_SHORT).show()
        } else {
            uploadToStorage()
        }
    }

    private fun uploadToStorage() {
        dialog!!.setMessage("Post being created...")
        dialog!!.show()
        val ref = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
        val id = ref.push().key
        val filePathAndName = "Images/Posts/$id"
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUrl = DATA.EMPTY + uriTask.result
                uploadInfoDB(uploadedImageUrl, id, ref)
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadInfoDB(uploadedImageUrl: String, id: String?, ref: DatabaseReference) {
        dialog!!.setMessage("Publishing...")
        dialog!!.show()
        val hashMap = HashMap<String, Any?>()
        hashMap["aname"] = DATA.APP_NAME
        hashMap["category"] = DATA.EMPTY + typePost
        hashMap["indications"] = binding!!.indications.text.toString().trim { it <= ' ' }
        hashMap["name"] = binding!!.name.text.toString().trim { it <= ' ' }
        hashMap["postid"] = id
        hashMap["postimage"] = uploadedImageUrl
        hashMap["postimage2"] = DATA.EMPTY
        hashMap["postimage3"] = DATA.EMPTY
        hashMap["postimage4"] = DATA.EMPTY
        hashMap["postimage5"] = DATA.EMPTY
        hashMap["postimage6"] = DATA.EMPTY
        hashMap["postimage7"] = DATA.EMPTY
        hashMap["postimage8"] = DATA.EMPTY
        hashMap["postimage9"] = DATA.EMPTY
        hashMap["postimage10"] = DATA.EMPTY
        hashMap["timeStamp"] = DATA.EMPTY + System.currentTimeMillis()
        hashMap["price"] = binding!!.price.text.toString().trim { it <= ' ' }
        hashMap["publisher"] = DATA.EMPTY + DATA.FirebaseUserUid
        hashMap["use"] = binding!!.howToUse.text.toString().trim { it <= ' ' }
        assert(id != null)
        ref.child(id!!).setValue(hashMap).addOnSuccessListener {
            dialog!!.dismiss()
            Toast.makeText(context, "uploaded", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e: Exception ->
            dialog!!.dismiss()
            Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT).show()
        }
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
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "Something went wrong! $error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}