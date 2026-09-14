package com.flatcode.beautytouchadmin.Unit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.ImageView
import coil3.load
import coil3.request.crossfade
import coil3.request.placeholder
import coil3.request.transformations
import com.flatcode.beautytouchadmin.R
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
                Image.load(Url) {
                    placeholder(R.color.image_profile)
                    crossfade(true)
                }
            }
        } catch (e: Exception) {
            Image.setImageResource(R.drawable.icon)
        }
    }

    fun GlideBlur(isUser: Boolean, context: Context?, Url: String?, Image: ImageView, level: Int) {
        try {
            if (Url == DATA.BASIC) {
                if (isUser) {
                    Image.setImageResource(R.drawable.basic_user)
                } else {
                    Image.setImageResource(R.drawable.icon)
                }
            } else {
                Image.load(Url) {
                    placeholder(R.color.image_profile)
                    transformations(SimpleBlurTransformation(level.toFloat()))
                }
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