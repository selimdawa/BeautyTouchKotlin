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
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityShoppingCentersAddBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage

class ShoppingCentersAddActivity : AppCompatActivity() {

    private var binding: ActivityShoppingCentersAddBinding? = null
    var activity: Activity? = null
    var context: Context = also { activity = it }
    private var imageUri: Uri? = null
    private var imageUri2: Uri? = null
    private var dialog: ProgressDialog? = null
    private val IMAGE_PIC = 1
    private val IMAGE_MAP = 2
    private var IMAGE_NUMBER = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCentersAddBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        dialog = ProgressDialog(context)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)

        binding!!.addImage.setOnClickListener {
            VOID.CropImageShoppingCenter(activity)
            IMAGE_NUMBER = IMAGE_PIC
        }
        binding!!.addImageTwo.setOnClickListener {
            VOID.CropImageShoppingCenter(activity)
            IMAGE_NUMBER = IMAGE_MAP
        }

        binding!!.toolbar.nameSpace.text = "Add a shopping center"
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }
        binding!!.go.setOnClickListener { validateData() }
    }

    private var name = DATA.EMPTY
    private var locationOne = DATA.EMPTY
    private var locationTwo = DATA.EMPTY
    private var locationThree = DATA.EMPTY
    private var numberPhone = DATA.EMPTY
    private fun validateData() {
        //get data
        name = binding!!.name.text.toString().trim { it <= ' ' }
        locationOne = binding!!.location.text.toString().trim { it <= ' ' }
        locationTwo = binding!!.location2.text.toString().trim { it <= ' ' }
        locationThree = binding!!.location3.text.toString().trim { it <= ' ' }
        numberPhone = binding!!.numberPhone.text.toString().trim { it <= ' ' }

        //validate data
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "Enter the name of the center", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(locationOne)) {
            Toast.makeText(context, "Enter the city name", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(locationTwo)) {
            Toast.makeText(context, "Enter the name of the neighborhood", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(locationThree)) {
            Toast.makeText(context, "Enter the street name", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(numberPhone)) {
            Toast.makeText(context, "Enter a phone number", Toast.LENGTH_SHORT).show()
        } else if (imageUri == null) {
            Toast.makeText(context, "There is no center image!", Toast.LENGTH_SHORT).show()
        } else if (imageUri2 == null) {
            Toast.makeText(context, "There is no location picture!", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        dialog!!.setMessage("Center image being created...")
        dialog!!.show()
        val ref = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS)
        val id = ref.push().key
        val filePathAndName = "Images/ShoppingCentres/image$id"
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val image = DATA.EMPTY + uriTask.result
                if (imageUri2 != null) {
                    uploadImage2(image, ref, id)
                } else {
                    uploadInfoDB(image, null, ref, id)
                }
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadImage2(image: String, ref: DatabaseReference, id: String?) {
        dialog!!.setMessage("Location image being created...")
        dialog!!.show()
        val filePathAndName = "Images/ShoppingCentres/" + id + "2"
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri2!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val image2 = DATA.EMPTY + uriTask.result
                uploadInfoDB(image, image2, ref, id)
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadInfoDB(image: String, image2: String?, ref: DatabaseReference, id: String?) {
        dialog!!.setMessage("Shopping center under construction...")
        dialog!!.show()
        val hashMap = HashMap<String, Any?>()
        hashMap["aname"] = DATA.APP_NAME
        hashMap["id"] = id
        hashMap["imageurl"] = image
        hashMap["imageurl2"] = image2
        hashMap["location"] = binding!!.location.text.toString().trim { it <= ' ' }
        hashMap["location2"] = binding!!.location2.text.toString().trim { it <= ' ' }
        hashMap["location3"] = binding!!.location3.text.toString().trim { it <= ' ' }
        hashMap["name"] = binding!!.name.text.toString().trim { it <= ' ' }
        hashMap["numberPhone"] = binding!!.numberPhone.text.toString().trim { it <= ' ' }
        hashMap["timeStamp"] = DATA.EMPTY + System.currentTimeMillis()
        hashMap["publisher"] = DATA.EMPTY + DATA.FirebaseUserUid
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
                if (IMAGE_NUMBER == IMAGE_PIC) {
                    imageUri = uri
                } else if (IMAGE_NUMBER == IMAGE_MAP) {
                    imageUri2 = uri
                }
                //imageUri = uri;
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            } else {
                VOID.CropImageShoppingCenter(activity)
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                if (IMAGE_NUMBER == IMAGE_PIC) {
                    imageUri = result.uri
                    binding!!.imageOne.setImageURI(imageUri)
                } else if (IMAGE_NUMBER == IMAGE_MAP) {
                    imageUri2 = result.uri
                    binding!!.imageTwo.setImageURI(imageUri2)
                }
                //imageUri = result.getUri();
                //binding.image.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "Something went wrong! $error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}