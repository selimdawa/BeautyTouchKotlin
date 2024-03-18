package com.flatcode.beautytouch.Unit

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.flatcode.beautytouch.Activity.MainActivity.Companion.mInterstitialAd
import com.flatcode.beautytouch.Model.ADs
import com.flatcode.beautytouch.R
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
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

    fun Intent1(context: Context?, c: Class<*>?) {
        val intent = Intent(context, c)
        context!!.startActivity(intent)
    }

    fun IntentExtra(context: Context?, c: Class<*>?, key: String?, value: String?) {
        val intent = Intent(context, c)
        intent.putExtra(key, value)
        context!!.startActivity(intent)
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
                    Image.setImageResource(R.drawable.basic_user)
                }
            } else {
                Glide.with(context!!).load(Url).centerCrop().placeholder(R.color.image_profile)
                    .into(Image)
            }
        } catch (e: Exception) {
            Image.setImageResource(R.drawable.basic_user)
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
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(
            context!!
        )
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

    fun BannerAd(context: Context?, adView: AdView, bannerName: String?) {
        if (context != null) MobileAds.initialize(context) { }

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_LOAD, 1)
                AdCount(DATA.FirebaseUserUid, bannerName, DATA.ADS_LOADED_COUNT)
            }

            override fun onAdOpened() {
                AdUserCount(DATA.FirebaseUserUid, DATA.AD_CLICK, 1)
                AdCount(DATA.FirebaseUserUid, bannerName, DATA.ADS_CLICKED_COUNT)
            }
        }
    }

    fun InterstitialAd(activity: Activity) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(
            activity, activity.resources.getString(R.string.admob_interstitial), adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    super.onAdLoaded(interstitialAd)
                    mInterstitialAd = interstitialAd
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    mInterstitialAd = null
                }
            })
    }

    fun InterstitialShow(activity: Activity, interstitialName: String?) {
        if (mInterstitialAd != null) {
            mInterstitialAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    mInterstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    mInterstitialAd = null
                    InterstitialAd(activity)
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                    AdUserCount(DATA.FirebaseUserUid, DATA.AD_LOAD, 1)
                    AdCount(DATA.FirebaseUserUid, interstitialName, DATA.ADS_LOADED_COUNT)
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    AdUserCount(DATA.FirebaseUserUid, DATA.AD_CLICK, 1)
                    AdCount(DATA.FirebaseUserUid, interstitialName, DATA.ADS_CLICKED_COUNT)
                }
            }
            mInterstitialAd!!.show(activity)
        } else {
        }
    }

    fun AdCount(userId: String?, bannerName: String?, key: String?) {
        val ref = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(userId!!)
        ref.child(bannerName!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //get views count
                var adCount = DATA.EMPTY + snapshot.child(key!!).value
                if (adCount == DATA.EMPTY || adCount == DATA.NULL) {
                    adCount = "0"
                }
                val newAdCount = adCount.toLong() + 1
                val hashMap = HashMap<String?, Any>()
                hashMap[key] = newAdCount
                val reference = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(userId)
                reference.child(bannerName).updateChildren(hashMap).addOnCompleteListener {
                    AdName(DATA.FirebaseUserUid, bannerName)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun AdRewardCount(userId: String?, key: String) {
        val ref = FirebaseDatabase.getInstance().getReference(DATA.USERS)
            .child(userId!!)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //get views count
                var adCount = DATA.EMPTY + snapshot.child(key).value
                if (adCount == DATA.EMPTY || adCount == DATA.NULL) {
                    adCount = "0"
                }
                val newAdCount = adCount.toLong() + 1
                val hashMap = HashMap<String, Any>()
                hashMap[key] = newAdCount
                ref.updateChildren(hashMap)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun AdUserCount(userId: String?, key: String?, number: Int) {
        val ref = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(
            userId!!
        )
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //get views count
                var adCount = DATA.EMPTY + snapshot.child(key!!).value
                if (adCount == DATA.EMPTY || adCount == DATA.NULL) {
                    adCount = "0"
                }
                val newAdCount = adCount.toLong() + number
                val hashMap = HashMap<String?, Any>()
                hashMap[key] = newAdCount
                val ref = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(userId)
                ref.updateChildren(hashMap)
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun AdName(userId: String?, bannerName: String?) {
        val ref = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(userId!!)
        ref.child(bannerName!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //get views count
                val item = snapshot.getValue(ADs::class.java)!!
                if (item.name == null) {
                    val hashMap = HashMap<String?, Any?>()
                    hashMap[DATA.NAME] = bannerName
                    val ref = FirebaseDatabase.getInstance().getReference(DATA.M_AD).child(userId)
                    ref.child(bannerName).updateChildren(hashMap)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun RateUs(activity: Activity) {
        val uri = Uri.parse("market://details?id=" + activity.packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        try {
            activity.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            activity.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.packageName)
                )
            )
        }
    }

    fun getFileExtension(uri: Uri?, context: Context): String? {
        val cR = context.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }
}