package com.flatcode.beautytouchadmin.utils

import android.app.Activity
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.transformations
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.databinding.DialogAboutBinding
import com.flatcode.beautytouchadmin.databinding.DialogLogoutBinding
import com.flatcode.beautytouchadmin.ui.auth.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import coil3.size.Size
import coil3.transform.Transformation
import java.text.MessageFormat

// --- Activity Extensions ---

fun Activity.dialogLogout() {
    if (this.isFinishing || this.isDestroyed) return

    val binding = DialogLogoutBinding.inflate(LayoutInflater.from(this))
    val dialog = Dialog(this)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    dialog.setCancelable(true)

    dialog.window?.let { window ->
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams().apply {
            copyFrom(window.attributes)
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        window.attributes = lp
    }

    binding.yes.setOnClickListener {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this@dialogLogout, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        this@dialogLogout.startActivity(intent)

        dialog.dismiss()
    }

    binding.no.setOnClickListener {
        dialog.cancel()
    }

    dialog.show()
}

fun Activity.dialogAboutApp() {
    if (this.isFinishing || this.isDestroyed) return

    val binding = DialogAboutBinding.inflate(LayoutInflater.from(this))
    val dialog = Dialog(this)

    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.setContentView(binding.root)
    dialog.setCancelable(true)

    dialog.window?.let { window ->
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams().apply {
            copyFrom(window.attributes)
            width = WindowManager.LayoutParams.WRAP_CONTENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
        }
        window.attributes = lp
    }

    dialog.show()
}

fun Activity.startCropImageSquare() {
    CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true)
        .setMinCropResultSize(DATA.MIN_SQUARE, DATA.MIN_SQUARE).setAspectRatio(1, 1)
        .setCropShape(CropImageView.CropShape.OVAL).start(this)
}

// --- Context Extensions ---

fun Context.openActivityAndClear(c: Class<*>) {
    val intent = Intent(this, c)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
}

fun Context.openActivity(c: Class<*>, vararg extras: Pair<String, String?>) {
    val intent = Intent(this, c)
    extras.forEach { intent.putExtra(it.first, it.second) }
    this.startActivity(intent)
}

fun Context.shareApp() {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "share app")
        putExtra(
            Intent.EXTRA_TEXT,
            "Download the app now from Google Play: https://google.com${this@shareApp.packageName}"
        )
    }
    this.startActivity(Intent.createChooser(shareIntent, "Choose how to share"))
}

fun Context.rateApp() {
    val packageName = this.packageName
    val marketUri = Uri.parse("market://details?id=$packageName")
    val webUri = Uri.parse("https://google.com")

    try {
        this.startActivity(Intent(Intent.ACTION_VIEW, marketUri))
    } catch (_: ActivityNotFoundException) {
        this.startActivity(Intent(Intent.ACTION_VIEW, webUri))
    }
}

fun Context.showMoreOptions(options: Array<String>, onOptionSelected: (Int) -> Unit) {
    AlertDialog.Builder(this).setTitle("Choose Options")
        .setItems(options) { _: DialogInterface?, which: Int ->
            onOptionSelected(which)
        }.show()
}

// --- ImageView Extensions ---

fun ImageView.loadImage(isUser: Boolean, url: String?) {
    try {
        if (url == DATA.BASIC) {
            if (isUser) {
                this.setImageResource(R.drawable.basic_user)
            } else {
                this.setImageResource(R.drawable.icon)
            }
        } else {
            this.load(url) {
                placeholder(R.color.image_profile)
                crossfade(true)
            }
        }
    } catch (_: Exception) {
        this.setImageResource(R.drawable.icon)
    }
}

fun ImageView.loadBlurImage(isUser: Boolean, url: String, level: Int) {
    try {
        if (url == DATA.BASIC) {
            if (isUser) {
                this.setImageResource(R.drawable.basic_user)
            } else {
                this.setImageResource(R.drawable.icon)
            }
        } else {
            this.load(url) {
                placeholder(R.color.image_profile)
                transformations(SimpleBlurTransformation(level.toFloat()))
            }
        }
    } catch (_: Exception) {
        this.setImageResource(R.drawable.icon)
    }
}

fun Uri.getFileExtension(context: Context): String {
    val cR: ContentResolver = context.contentResolver
    val mime: MimeTypeMap = MimeTypeMap.getSingleton()
    return mime.getExtensionFromMimeType(cR.getType(this))!!
}
