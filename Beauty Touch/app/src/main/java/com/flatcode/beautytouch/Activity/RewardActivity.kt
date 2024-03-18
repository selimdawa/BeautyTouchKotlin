package com.flatcode.beautytouch.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouch.Model.Tools
import com.flatcode.beautytouch.R
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.THEME.setThemeOfApp
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.Unit.VOID.AdRewardCount
import com.flatcode.beautytouch.Unitimport.CLASS
import com.flatcode.beautytouch.databinding.ActivityRewardBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class RewardActivity : AppCompatActivity() {

    private var activity: Activity? = null
    private val context: Context = also { activity = it }
    private var binding: ActivityRewardBinding? = null

    var mRewardedAd: RewardedAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityRewardBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.earn_points)
        binding!!.leaderboardCard.setOnClickListener {
            VOID.Intent1(context, CLASS.LEADERBOARD)
        }
        binding!!.leaderboardCardOld.setOnClickListener {
            VOID.Intent1(context, CLASS.LEADERBOARD_OLD)
        }
        MobileAds.initialize(context) { }
        loadRewardedAd()
    }

    private fun loadRewardedAd() {
        RewardedAd.load(
            context, resources.getString(R.string.admob_reward), AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    mRewardedAd = rewardedAd
                }
            })
    }

    private fun showRewardedAd() {
        if (mRewardedAd != null) {
            mRewardedAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdClicked() {
                    super.onAdClicked()
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    mRewardedAd = null
                    Reward()
                    loadRewardedAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    mRewardedAd = null
                }

                override fun onAdImpression() {
                    super.onAdImpression()
                }

                override fun onAdShowedFullScreenContent() {
                    super.onAdShowedFullScreenContent()
                }
            }
            mRewardedAd!!.show(activity!!) { rewardItem: RewardItem? -> }
        } else {
        }
    }

    private fun loadAndShowRewardedAd() {
        val dialog = ProgressDialog(context)
        dialog.setTitle("Please wait")
        dialog.setMessage("Loading Rewarded Ad")
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        RewardedAd.load(
            context, resources.getString(R.string.admob_reward), AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    super.onAdLoaded(rewardedAd)
                    mRewardedAd = rewardedAd
                    dialog.dismiss()
                    showRewardedAd()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    super.onAdFailedToLoad(loadAdError)
                    mRewardedAd = null
                    dialog.dismiss()
                }
            })
    }

    private fun getNrPoints(year: String?, session: String?) {
        val reference =
            FirebaseDatabase.getInstance().getReference(DATA.USERS).child(DATA.FirebaseUserUid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val key = year + "_" + session
                val value = DATA.EMPTY + dataSnapshot.child(key).value
                if (dataSnapshot.child(key).exists()) binding!!.myPoints.text =
                    MessageFormat.format("My Points : {0}", value) else binding!!.myPoints.text =
                    "My Points : 0"
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun SessionInfo() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools = dataSnapshot.getValue(Tools::class.java)!!
                val year = tools.year
                val session = tools.sessionNumber
                val oldYear = tools.oldYear
                val oldSession = tools.oldSessionNumber
                if (session != oldSession || year != oldYear) binding!!.leaderboardCardOld.visibility =
                    View.VISIBLE else binding!!.leaderboardCardOld.visibility = View.GONE
                binding!!.sessionInfo.text = MessageFormat.format("{0} | {1}", year, session)
                binding!!.sessionInfoOld.text =
                    MessageFormat.format("{0} | {1}", oldYear, oldSession)
                getNrPoints(year, session)
                binding!!.rewardCard.setOnClickListener {
                    loadAndShowRewardedAd()
                    Toast.makeText(
                        context, "The ad is loaded, click again if it does not appear",
                        Toast.LENGTH_SHORT
                    ).show()
                    getNrPoints(year, session)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun Reward() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools = dataSnapshot.getValue(Tools::class.java)!!
                val year = tools.year
                val session = tools.sessionNumber
                AdRewardCount(DATA.FirebaseUserUid, DATA.EMPTY + year + "_" + DATA.EMPTY + session)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onResume() {
        SessionInfo()
        super.onResume()
    }

    override fun onRestart() {
        SessionInfo()
        super.onRestart()
    }
}