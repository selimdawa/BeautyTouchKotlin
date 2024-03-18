package com.flatcode.beautytouch.Activity

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.flatcode.beautytouch.BuildConfig
import com.flatcode.beautytouch.Fragment.HairProductsFragment
import com.flatcode.beautytouch.Fragment.HomeFragment
import com.flatcode.beautytouch.Fragment.ShoppingCentersFragment
import com.flatcode.beautytouch.Fragment.SkinProductsFragment
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Model.Tools
import com.flatcode.beautytouch.Model.User
import com.flatcode.beautytouch.R
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.THEME
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.Unitimport.CLASS
import com.flatcode.beautytouch.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
    SharedPreferences.OnSharedPreferenceChangeListener {

    private var binding: ActivityMainBinding? = null
    var activity: Activity? = null
    var context: Context = also { activity = it }
    var home = "Home Page"
    var skin_product = "Skin Products"
    var hair_product = "Hair Products"
    var shopping_center = "Shopping Centers"
    var number_product = DATA.EMPTY
    var meowBottomNavigation: MeowBottomNavigation? = null
    var publisher: String = DATA.PUBLISHER_NAME
    var aname: String = DATA.APP_NAME

    override fun onCreate(savedInstanceState: Bundle?) {
        PreferenceManager.getDefaultSharedPreferences(baseContext)
            .registerOnSharedPreferenceChangeListener(this)
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        // Color Mode ----------------------------- Start
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingFragment())
            .commit()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_night, Setting2Fragment())
            .commit()
        // Color Mode -------------------------------- End

        binding!!.toolbar.image.setOnClickListener { VOID.Intent1(context, CLASS.PROFILE) }
        binding!!.toolbar.drawer.setOnClickListener {
            binding!!.drawerLayout.openDrawer(GravityCompat.START)
        }

        MobileAds.initialize(this) { }
        VOID.InterstitialAd(activity!!)

        binding!!.myProfile.setOnClickListener {
            VOID.Intent1(context, CLASS.PROFILE)
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        }
        binding!!.favorites.setOnClickListener { VOID.Intent1(context, CLASS.FAVORITES) }
        binding!!.messenger.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("https://wa.me/message/E2YOU4NVTIEAD1")
            startActivity(i)
        }
        binding!!.reward.setOnClickListener { VOID.Intent1(context, CLASS.REWARD) }
        binding!!.aboutApp.setOnClickListener { showDialogAboutApp() }
        binding!!.shareApp.setOnClickListener { ShareApp() }
        binding!!.aboutMy.setOnClickListener { showDialogAboutMy() }
        binding!!.logout.setOnClickListener { showDialogLogout() }

        meowBottomNavigation = binding!!.bottomNavigation
        meowBottomNavigation!!.add(MeowBottomNavigation.Model(1, R.drawable.ic_skin))
        meowBottomNavigation!!.add(MeowBottomNavigation.Model(2, R.drawable.ic_home))
        meowBottomNavigation!!.add(MeowBottomNavigation.Model(3, R.drawable.ic_hair))
        meowBottomNavigation!!.add(MeowBottomNavigation.Model(4, R.drawable.ic_shopping_centers))

        meowBottomNavigation!!.setOnShowListener { item: MeowBottomNavigation.Model ->
            var fragment: Fragment? = null
            when (item.id) {
                1 -> fragment = SkinProductsFragment()
                2 -> fragment = HomeFragment()
                3 -> fragment = HairProductsFragment()
                4 -> fragment = ShoppingCentersFragment()
            }
            loadFragment(fragment)
        }

        meowBottomNavigation!!.setCount(1, number_product)
        meowBottomNavigation!!.setCount(3, number_product)
        meowBottomNavigation!!.setCount(4, number_product)
        meowBottomNavigation!!.show(2, true)

        meowBottomNavigation!!.setOnClickMenuListener { item: MeowBottomNavigation.Model ->
            when (item.id) {
                1 -> Toast.makeText(applicationContext, skin_product, Toast.LENGTH_SHORT)
                    .show()

                2 -> {
                    Toast.makeText(applicationContext, home, Toast.LENGTH_SHORT).show()
                }

                3 -> Toast.makeText(applicationContext, hair_product, Toast.LENGTH_SHORT)
                    .show()

                4 -> {
                    Toast.makeText(applicationContext, shopping_center, Toast.LENGTH_SHORT)
                        .show()
                    VOID.InterstitialShow(activity!!, DATA.INTERSTITIAL_HOME)
                }
            }
        }
        meowBottomNavigation!!.setOnReselectListener { item: MeowBottomNavigation.Model ->
            when (item.id) {
                1 -> Toast.makeText(applicationContext, skin_product, Toast.LENGTH_SHORT)
                    .show()

                2 -> Toast.makeText(applicationContext, home, Toast.LENGTH_SHORT).show()
                3 -> Toast.makeText(applicationContext, hair_product, Toast.LENGTH_SHORT)
                    .show()

                4 -> Toast.makeText(applicationContext, shopping_center, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        val toggle = ActionBarDrawerToggle(
            this, binding!!.drawerLayout,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding!!.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding!!.imageDrawer.setOnClickListener { VOID.Intent1(context, CLASS.PROFILE) }
        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(baseContext)
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
            binding!!.imageNight.setBackgroundResource(R.drawable.sun)
        } else if (sharedPreferences.getString("color_option", "NIGHT_ONE") == "NIGHT_ONE" ||
            sharedPreferences.getString("color_option", "NIGHT_TWO") == "NIGHT_TWO" ||
            sharedPreferences.getString("color_option", "NIGHT_THREE") == "NIGHT_THREE" ||
            sharedPreferences.getString("color_option", "NIGHT_FOUR") == "NIGHT_FOUR" ||
            sharedPreferences.getString("color_option", "NIGHT_FIVE") == "NIGHT_FIVE" ||
            sharedPreferences.getString("color_option", "NIGHT_SIX") == "NIGHT_SIX" ||
            sharedPreferences.getString("color_option", "NIGHT_SEVEN") == "NIGHT_SEVEN"
        ) {
            binding!!.imageNight.setBackgroundResource(R.drawable.moon)
        }

        nrSkin
        nrHair
        nrShoppingCenters
        userInfo()
    }

    private val nrSkin: Unit
        get() {
            val reference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(DATA.POSTS)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var i = 0
                    for (snapshot in dataSnapshot.children) {
                        val post: Post = snapshot.getValue(Post::class.java)!!
                        if (post.category == DATA.SKIN_PRODUCTS) if (post.publisher == publisher)
                            if (post.aname == aname) i++
                        binding!!.numberProductSkin.text = MessageFormat.format("{0}", i)
                        meowBottomNavigation!!.setCount(
                            1, binding!!.numberProductSkin.text as String
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    private val nrHair: Unit
        get() {
            val reference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(DATA.POSTS)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var i = 0
                    for (snapshot in dataSnapshot.children) {
                        val post: Post = snapshot.getValue(Post::class.java)!!
                        if (post.category == DATA.HAIR_PRODUCTS) if (post.publisher == publisher)
                            if (post.aname == aname) i++
                        binding!!.numberProductHair.text = MessageFormat.format("{0}", i)
                        meowBottomNavigation!!.setCount(
                            3, binding!!.numberProductHair.text as String
                        )
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    private val nrShoppingCenters: Unit
        get() {
            val reference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    binding!!.numberShoppingCenters.text =
                        MessageFormat.format("{0}", dataSnapshot.childrenCount)
                    meowBottomNavigation!!.setCount(
                        4, binding!!.numberShoppingCenters.text as String
                    )
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    private fun loadFragment(fragment: Fragment?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment!!)
            .commit()
    }

    override fun onBackPressed() {
        if (binding!!.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.dialog_closeapp)
            dialog.setCancelable(true)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window!!.attributes)
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.findViewById<View>(R.id.yes).setOnClickListener { finish() }
            dialog.findViewById<View>(R.id.no).setOnClickListener { dialog.cancel() }
            dialog.show()
            dialog.window!!.attributes = lp
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding!!.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun ShareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "share app")
        shareIntent.putExtra(
            Intent.EXTRA_TEXT,
            " Beauty Touch beauty care application, download it now from Google Play " + " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
        )
        startActivity(Intent.createChooser(shareIntent, "Choose how to share"))
    }

    private fun userInfo() {
        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(DATA.USERS).child(DATA.FirebaseUserUid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user: User = dataSnapshot.getValue(User::class.java)!!

                Glide.with(context).load(user.imageurl).into(binding!!.imageDrawer)
                Glide.with(context).load(user.imageurl).into(binding!!.toolbar.image)
                binding!!.name.text = user.username
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun showDialogAboutMy() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_about)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        val image = dialog.findViewById<ImageView>(R.id.image)
        val text: TextView = dialog.findViewById(R.id.text)
        AboutMe(image, text)
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun AboutMe(image: ImageView, text: TextView) {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools: Tools = dataSnapshot.getValue(Tools::class.java)!!
                val aboutMe: String = tools.aboutMe!!
                val imageMe: String = tools.imageMe!!

                VOID.Glide(true, context, imageMe, image)
                text.text = aboutMe
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun showDialogAboutApp() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_app)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.findViewById<View>(R.id.linear_rate).setOnClickListener { VOID.RateUs(activity!!) }
        dialog.findViewById<View>(R.id.facebook_design)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    startActivity(openFacebookIntent)
                }

                val openFacebookIntent: Intent
                    get() = try {
                        getPackageManager().getPackageInfo("com.facebook.katana", 0)
                        Intent(Intent.ACTION_VIEW, Uri.parse(DATA.FB_DESINGER))
                    } catch (e: Exception) {
                        Intent(Intent.ACTION_VIEW, Uri.parse(DATA.FB_DESINGER_2))
                    }
            })
        dialog.findViewById<View>(R.id.facebook_programmer)
            .setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    startActivity(openFacebookIntent)
                }

                val openFacebookIntent: Intent
                    get() = try {
                        getPackageManager().getPackageInfo("com.facebook.katana", 0)
                        Intent(Intent.ACTION_VIEW, Uri.parse(DATA.FB_PROGRAMMER))
                    } catch (e: Exception) {
                        Intent(Intent.ACTION_VIEW, Uri.parse(DATA.FB_PROGRAMMER_2))
                    }
            })
        dialog.show()
        dialog.window!!.attributes = lp
    }

    private fun showDialogLogout() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_logout)
        dialog.setCancelable(true)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.findViewById<View>(R.id.yes).setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            VOID.IntentClear(context, CLASS.LOGIN)
            finish()
        }
        dialog.findViewById<View>(R.id.no).setOnClickListener { dialog.cancel() }
        dialog.show()
        dialog.window!!.attributes = lp
    }

    // Color Mode ----------------------------- Start
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == DATA.COLOR_OPTION) {
            this.recreate()
        }
    }

    class SettingFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    class Setting2Fragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences_night, rootKey)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_CODE) {
            this.recreate()
        }
    } // Color Mode -------------------------------- End

    companion object {
        private const val SETTINGS_CODE = 234
        var mInterstitialAd: InterstitialAd? = null
    }
}