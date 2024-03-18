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
import com.flatcode.beautytouchadmin.Model.Tools
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityToolsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage

class ToolsActivity : AppCompatActivity() {

    private var binding: ActivityToolsBinding? = null
    var activity: Activity? = null
    var context: Context = also { activity = it }
    private var imageUri: Uri? = null
    private var imageUri2: Uri? = null
    private var imageUri3: Uri? = null
    private var imageUri4: Uri? = null
    private var dialog: ProgressDialog? = null
    private val IMAGE_NOW = 1
    private val IMAGE_OLD = 2
    private val LOGO_NOW = 3
    private val LOGO_OLD = 4
    private var IMAGE_NUMBER = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityToolsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        dialog = ProgressDialog(context)
        dialog!!.setTitle("Please wait...")
        dialog!!.setCanceledOnTouchOutside(false)

        binding!!.editImageSessionNow.setOnClickListener {
            VOID.CropImageSession(activity)
            IMAGE_NUMBER = IMAGE_NOW
        }
        binding!!.editImageSessionOld.setOnClickListener {
            VOID.CropImageSession(activity)
            IMAGE_NUMBER = IMAGE_OLD
        }
        binding!!.editLogoSessionNow.setOnClickListener {
            VOID.CropImageSession(activity)
            IMAGE_NUMBER = LOGO_NOW
        }
        binding!!.editLogoSessionOld.setOnClickListener {
            VOID.CropImageSession(activity)
            IMAGE_NUMBER = LOGO_OLD
        }
        binding!!.toolbar.nameSpace.setText(R.string.tools)
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }
        binding!!.go.setOnClickListener { validateData() }
    }

    private var sessionNow = DATA.EMPTY
    private var sessionOld = DATA.EMPTY
    private var sessionNumberNow = DATA.EMPTY
    private var sessionNumberOld = DATA.EMPTY
    private var yearNow = DATA.EMPTY
    private var yearOld = DATA.EMPTY
    private fun validateData() {
        //get data
        sessionNow = binding!!.sessionNow.text.toString().trim { it <= ' ' }
        sessionOld = binding!!.sessionOld.text.toString().trim { it <= ' ' }
        sessionNumberNow = binding!!.sessionNumberNow.text.toString().trim { it <= ' ' }
        sessionNumberOld = binding!!.sessionNumberOld.text.toString().trim { it <= ' ' }
        yearNow = binding!!.yearNow.text.toString().trim { it <= ' ' }
        yearOld = binding!!.yearOld.text.toString().trim { it <= ' ' }

        //validate data
        if (TextUtils.isEmpty(sessionNow)) {
            Toast.makeText(context, "Enter the Session number", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(sessionNumberNow)) {
            Toast.makeText(context, "Enter the Session name", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(yearNow)) {
            Toast.makeText(context, "Enter the year", Toast.LENGTH_SHORT).show()
        } else {
            if (imageUri == null && imageUri2 == null && imageUri3 == null && imageUri4 == null) {
                updatePost(DATA.EMPTY, DATA.EMPTY, DATA.EMPTY, DATA.EMPTY)
            } else {
                if (imageUri != null) uploadImage() else if (imageUri2 != null) uploadImage2(null) else if (imageUri3 != null) uploadImage3(
                    null, null
                ) else if (imageUri4 != null) uploadImage4(null, null, null)
            }
        }
    }

    private fun uploadImage() {
        dialog!!.setMessage("The current session image is being updated...")
        dialog!!.show()
        val filePathAndName = "Images/Session/sessionNow"
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val image = DATA.EMPTY + uriTask.result
                if (imageUri2 != null) {
                    uploadImage2(image)
                } else if (imageUri3 != null) {
                    uploadImage3(image, null)
                } else if (imageUri4 != null) {
                    uploadImage4(image, null, null)
                } else {
                    updatePost(image, null, null, null)
                }
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadImage2(image: String?) {
        dialog!!.setMessage("The previous session image is being updated...")
        dialog!!.show()
        val filePathAndName = "Images/Session/sessionOld"
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri2!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val image2 = DATA.EMPTY + uriTask.result
                if (imageUri3 != null) {
                    uploadImage3(image, image2)
                } else if (imageUri4 != null) {
                    uploadImage4(image, image2, null)
                } else {
                    updatePost(image, image2, null, null)
                }
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadImage3(image: String?, image2: String?) {
        dialog!!.setMessage("The current session logo is being updated...")
        dialog!!.show()
        val filePathAndName = "Images/Logo/logoNow"
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri3!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val image3 = DATA.EMPTY + uriTask.result
                if (imageUri4 != null) {
                    uploadImage4(image, image2, image3)
                } else {
                    updatePost(image, image2, image3, null)
                }
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun uploadImage4(image: String?, image2: String?, image3: String?) {
        dialog!!.setMessage("The logo of the previous session is being updated...")
        dialog!!.show()
        val filePathAndName = "Images/Logo/logoOld"
        val reference = FirebaseStorage.getInstance()
            .getReference(filePathAndName + DATA.DOT + VOID.getFileExtension(imageUri, context))
        reference.putFile(imageUri4!!)
            .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                val image4 = DATA.EMPTY + uriTask.result
                updatePost(image, image2, image3, image4)
            }.addOnFailureListener { e: Exception ->
                dialog!!.dismiss()
                Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun updatePost(image: String?, image2: String?, image3: String?, image4: String?) {
        dialog!!.setMessage("Editing....")
        dialog!!.show()
        val hashMap = HashMap<String, Any>()
        hashMap["session"] = DATA.EMPTY + sessionNow
        hashMap["sessionNumber"] = DATA.EMPTY + sessionNumberNow
        hashMap["year"] = DATA.EMPTY + yearNow
        hashMap["oldSession"] = DATA.EMPTY + sessionOld
        hashMap["oldSessionNumber"] = DATA.EMPTY + sessionNumberOld
        hashMap["oldYear"] = DATA.EMPTY + yearOld
        if (imageUri != null) hashMap["imageSession"] = DATA.EMPTY + image
        if (imageUri2 != null) hashMap["oldImageSession"] = DATA.EMPTY + image2
        if (imageUri3 != null) hashMap["imageLogo"] = DATA.EMPTY + image3
        if (imageUri4 != null) hashMap["oldImageLogo"] = DATA.EMPTY + image4
        val reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.updateChildren(hashMap).addOnSuccessListener {
            dialog!!.dismiss()
            Toast.makeText(context, "Modified", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e: Exception ->
            dialog!!.dismiss()
            Toast.makeText(context, "Something went wrong! " + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun Data() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools = dataSnapshot.getValue(Tools::class.java)!!
                val sessionNow = tools.session
                val sessionOld = tools.oldSession
                val sessionNumberNow = tools.sessionNumber
                val sessionNumberOld = tools.oldSessionNumber
                val yearNow = tools.year
                val yearOld = tools.oldYear
                val ImageNow = tools.imageSession
                val ImageOld = tools.oldImageSession
                val logoNow = tools.imageLogo
                val logoOld = tools.oldImageLogo

                VOID.Glide(false, context, ImageNow, binding!!.imageSessionNow)
                VOID.Glide(false, context, ImageOld, binding!!.imageSessionOld)
                VOID.Glide(false, context, logoNow, binding!!.logoSessionNow)
                VOID.Glide(false, context, logoOld, binding!!.logoSessionOld)
                binding!!.sessionNow.setText(sessionNow)
                binding!!.sessionOld.setText(sessionOld)
                binding!!.sessionNumberNow.setText(sessionNumberNow)
                binding!!.sessionNumberOld.setText(sessionNumberOld)
                binding!!.yearNow.setText(yearNow)
                binding!!.yearOld.setText(yearOld)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            val uri = CropImage.getPickImageResultUri(context, data)
            if (CropImage.isReadExternalStoragePermissionsRequired(context, uri)) {
                when (IMAGE_NUMBER) {
                    IMAGE_NOW -> {
                        imageUri = uri
                    }

                    IMAGE_OLD -> {
                        imageUri2 = uri
                    }

                    LOGO_NOW -> {
                        imageUri3 = uri
                    }

                    LOGO_OLD -> {
                        imageUri4 = uri
                    }
                }
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            } else {
                VOID.CropImageSquare(activity)
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                when (IMAGE_NUMBER) {
                    IMAGE_NOW -> {
                        imageUri = result.uri
                        binding!!.imageSessionNow.setImageURI(imageUri)
                    }

                    IMAGE_OLD -> {
                        imageUri2 = result.uri
                        binding!!.imageSessionOld.setImageURI(imageUri2)
                    }

                    LOGO_NOW -> {
                        imageUri3 = result.uri
                        binding!!.logoSessionNow.setImageURI(imageUri3)
                    }

                    LOGO_OLD -> {
                        imageUri4 = result.uri
                        binding!!.logoSessionOld.setImageURI(imageUri4)
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Toast.makeText(this, "Something went wrong! $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRestart() {
        Data()
        super.onRestart()
    }

    override fun onResume() {
        Data()
        super.onResume()
    }
}