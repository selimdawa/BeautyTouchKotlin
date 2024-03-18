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
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityPostAddBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage

class PostEditActivity : AppCompatActivity() {

    private var binding: ActivityPostAddBinding? = null
    var activity: Activity? = null
    var context: Context = also { activity = it }
    var id: String? = null
    private var imageUri: Uri? = null
    private var dialog: ProgressDialog? = null
    var typePost: String? = DATA.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityPostAddBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val intent = intent
        id = intent.getStringExtra(DATA.POST_ID)

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

        loadPostInfo()
        binding!!.toolbar.nameSpace.text = "Edit post"
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }
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
        } else {
            if (imageUri == null) {
                updatePost(DATA.EMPTY)
            } else {
                uploadImage()
            }
        }
    }

    private fun uploadImage() {
        dialog!!.setMessage("Post is updating")
        dialog!!.show()
        val filePathAndName = "Images/Posts/$id"
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val uploadedImageUrl = DATA.EMPTY + uriTask.result
                updatePost(uploadedImageUrl)
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun updatePost(imageUrl: String) {
        dialog!!.setMessage("Editing....")
        dialog!!.show()
        val hashMap = HashMap<String, Any>()
        hashMap[DATA.NAME] = DATA.EMPTY + name
        hashMap["price"] = DATA.EMPTY + price
        hashMap["indications"] = DATA.EMPTY + indications
        hashMap["use"] = DATA.EMPTY + howToUse
        hashMap["category"] = DATA.EMPTY + typePost
        if (imageUri != null) {
            hashMap["postimage"] = DATA.EMPTY + imageUrl
        }
        val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
        reference.child(id!!).updateChildren(hashMap).addOnSuccessListener {
            dialog!!.dismiss()
            Toast.makeText(context, "Modified", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e: Exception ->
            dialog!!.dismiss()
            Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPostInfo() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
        reference.child(id!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val item = snapshot.getValue(Post::class.java)!!
                val name = item.name
                val price = item.price
                val indications = item.indications
                val howToUse = item.use
                val category = item.category
                val image = item.postimage

                typePost = category
                VOID.Glide(true, context, image, binding!!.image)
                binding!!.name.setText(name)
                binding!!.price.setText(price)
                binding!!.indications.setText(indications)

                binding!!.howToUse.setText(howToUse)
                if (category == "Skin Products") {
                    binding!!.typeOne.text = "Skin Products ✓"
                    binding!!.typeTwo.text = "Hair Products"
                    typePost = category
                } else if (category == "Hair Products") {
                    binding!!.typeOne.text = "Skin Products"
                    binding!!.typeTwo.text = "Hair Products ✓"
                    typePost = category
                }
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
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "Something went wrong! $error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}