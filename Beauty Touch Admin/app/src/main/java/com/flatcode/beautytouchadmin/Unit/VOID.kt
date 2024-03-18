package com.flatcode.beautytouchadmin.Unit

import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.Model.ShoppingCenter
import com.flatcode.beautytouchadmin.Model.Tools
import com.flatcode.beautytouchadmin.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

object VOID {
    fun IntentClear(context: Context, c: Class<*>?) {
        val intent = Intent(context, c)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    fun Intent1(context: Context, c: Class<*>?) {
        val intent = Intent(context, c)
        context.startActivity(intent)
    }

    fun IntentExtra(context: Context, c: Class<*>?, key: String?, value: String?) {
        val intent = Intent(context, c)
        intent.putExtra(key, value)
        context.startActivity(intent)
    }

    fun IntentExtra2(
        context: Context, c: Class<*>?, key: String?, value: String?, key2: String?, value2: String?
    ) {
        val intent = Intent(context, c)
        intent.putExtra(key, value)
        intent.putExtra(key2, value2)
        context.startActivity(intent)
    }

    fun IntentExtra3(
        context: Context, c: Class<*>?, key: String?, value: String?,
        key2: String?, value2: String?, key3: String?, value3: String?
    ) {
        val intent = Intent(context, c)
        intent.putExtra(key, value)
        intent.putExtra(key2, value2)
        intent.putExtra(key3, value3)
        context.startActivity(intent)
    }

    fun Glide(isUser: Boolean, context: Context?, Url: String?, Image: ImageView) {
        try {
            if (Url == DATA.BASIC) {
                if (isUser) {
                    Image.setImageResource(R.drawable.basic_user)
                } else {
                    Image.setImageResource(R.drawable.icon)
                }
            } else {
                Glide.with(context!!).load(Url).centerCrop().placeholder(R.color.image_profile)
                    .into(Image)
            }
        } catch (e: Exception) {
            Image.setImageResource(R.drawable.icon)
        }
    }

    fun CropImageSquare(activity: Activity?) {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setMinCropResultSize(DATA.MIN_SQUARE, DATA.MIN_SQUARE)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(activity!!)
    }

    fun Intro(context: Context?, background: ImageView, backWhite: ImageView, backDark: ImageView) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context!!)
        if (sharedPreferences.getString("color_option", "ONE") == "ONE" ||
            sharedPreferences.getString("color_option", "TWO") == "TWO" ||
            sharedPreferences.getString("color_option", "THREE") == "THREE" ||
            sharedPreferences.getString("color_option", "FOUR") == "FOUR" ||
            sharedPreferences.getString("color_option", "FIVE") == "FIVE" ||
            sharedPreferences.getString("color_option", "SIX") == "SIX" ||
            sharedPreferences.getString("color_option", "SEVEN") == "SEVEN" ||
            sharedPreferences.getString("color_option", "EIGHT") == "EIGHT" ||
            sharedPreferences.getString("color_option", "NINE") == "NINE" ||
            sharedPreferences.getString("color_option", "TEEN") == "TEEN"
        ) {
            background.setImageResource(R.drawable.background_day)
            backWhite.visibility = View.VISIBLE
            backDark.visibility = View.GONE
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE") == "NIGHT_ONE" ||
            sharedPreferences.getString("color_option", "NIGHT_TWO") == "NIGHT_TWO" ||
            sharedPreferences.getString("color_option", "NIGHT_THREE") == "NIGHT_THREE" ||
            sharedPreferences.getString("color_option", "NIGHT_FOUR") == "NIGHT_FOUR" ||
            sharedPreferences.getString("color_option", "NIGHT_FIVE") == "NIGHT_FIVE" ||
            sharedPreferences.getString("color_option", "NIGHT_SIX") == "NIGHT_SIX" ||
            sharedPreferences.getString("color_option", "NIGHT_SEVEN") == "NIGHT_SEVEN"
        ) {
            background.setImageResource(R.drawable.background_night)
            backWhite.visibility = View.GONE
            backDark.visibility = View.VISIBLE
        }
    }

    fun getNrFromServer(server: String, name: TextView) {
        val reference = FirebaseDatabase.getInstance().getReference(server)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val i: Int
                val Name = name.text.toString()
                if (server == DATA.USERS) {
                    i = (dataSnapshot.childrenCount - 1).toInt()
                    name.text = "$Name ( $i )"
                } else if (server == DATA.M_TOOLS) {
                    val tools = dataSnapshot.getValue(Tools::class.java)
                    if (Name.contains("Old")) {
                        name.text = Name + " " + tools!!.oldYear + " | " + tools.oldSession
                    } else {
                        name.text = Name + " " + tools!!.year + " | " + tools.session
                    }
                } else {
                    i = dataSnapshot.childrenCount.toInt()
                    name.text = "$Name ( $i )"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun moreOptionDialog(context: Context, item: Post?) {
        val id = item!!.postid
        val name = item.name

        //options to show in dialog
        val options = arrayOf("Edit", "Delete")

        //alert dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose...").setItems(options) { dialog: DialogInterface?, which: Int ->
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                IntentExtra(context, CLASS.POST_EDIT, DATA.POST_ID, id)
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.EMPTY + id, DATA.EMPTY + name, false)
            }
        }.show()
    }

    fun moreShoppingCenters(context: Context, item: ShoppingCenter?) {
        val id = item!!.id
        val name = item.name

        //options to show in dialog
        val options = arrayOf("Edit", "Delete")

        //alert dialog
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Choose...").setItems(options) { dialog: DialogInterface?, which: Int ->
            //handle dialog option click
            if (which == 0) {
                //Edit clicked ,Open new activity to edit the book info
                IntentExtra(context, CLASS.SHOPPING_CENTRES_EDIT, DATA.SHOPPING_CENTER_ID, id)
            } else if (which == 1) {
                //Delete Clicked
                dialogOptionDelete(context, DATA.EMPTY + id, DATA.EMPTY + name, true)
            }
        }.show()
    }

    fun dialogOptionDelete(context: Context?, id: String?, name: String, isPharmacy: Boolean) {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val title = dialog.findViewById<TextView>(R.id.title)
        if (isPharmacy) {
            title.setText(R.string.do_you_want_to_delete_the_pharmacy)
        } else {
            title.setText(R.string.do_you_want_to_delete_the_post)
        }
        dialog.findViewById<View>(R.id.yes).setOnClickListener {
            if (isPharmacy) {
                delete(dialog, context, DATA.SHOPPING_CENTERS, id, name)
            } else {
                delete(dialog, context, DATA.POSTS, id, name)
            }
        }
        dialog.findViewById<View>(R.id.no).setOnClickListener { dialog.dismiss() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    fun delete(
        dialogDelete: Dialog, context: Context?, database: String?, id: String?, name: String
    ) {
        val dialog = ProgressDialog(context)
        dialog.setTitle("Please wait")
        dialog.setMessage("is deleted  $name ...")
        dialog.show()
        val reference = FirebaseDatabase.getInstance().getReference(database!!)
        reference.child(id!!).removeValue().addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(context, "Post deleted successfully...", Toast.LENGTH_SHORT).show()
            dialogDelete.dismiss()
        }.addOnFailureListener { e: Exception ->
            dialog.dismiss()
            dialogDelete.dismiss()
            Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun CropImageSlider(activity: Activity?) {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setMinCropResultSize(DATA.MIX_SLIDER_X, DATA.MIX_SLIDER_Y)
            .setAspectRatio(16, 9)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(activity!!)
    }

    fun CropImageShoppingCenter(activity: Activity?) {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setMinCropResultSize(DATA.MIX_SLIDER_X, DATA.MIX_SLIDER_Y)
            .setAspectRatio(2, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(activity!!)
    }

    fun CropImageSession(activity: Activity?) {
        CropImage.activity()
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .setMinCropResultSize(DATA.MIN_SQUARE, DATA.MIN_SQUARE)
            .setAspectRatio(1, 1)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(activity!!)
    }

    fun getFileExtension(uri: Uri?, context: Context): String? {
        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }
}