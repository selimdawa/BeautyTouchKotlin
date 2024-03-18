package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.flatcode.beautytouchadmin.Adapter.MainAdapter
import com.flatcode.beautytouchadmin.Model.Main
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private var binding: ActivityMainBinding? = null
    var list: MutableList<Main>? = null
    var adapter: MainAdapter? = null
    var context: Context = this@MainActivity

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
            .replace(R.id.settings, SettingsFragment())
            .commit()
        // Color Mode -------------------------------- End
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(baseContext)
        if (sharedPreferences.getString(
                "color_option",
                "ONE"
            ) == "ONE" || sharedPreferences.getString(
                "color_option",
                "TWO"
            ) == "TWO" || sharedPreferences.getString(
                "color_option",
                "THREE"
            ) == "THREE" || sharedPreferences.getString(
                "color_option",
                "FOUR"
            ) == "FOUR" || sharedPreferences.getString(
                "color_option",
                "FIVE"
            ) == "FIVE" || sharedPreferences.getString(
                "color_option",
                "SIX"
            ) == "SIX" || sharedPreferences.getString(
                "color_option",
                "SEVEN"
            ) == "SEVEN" || sharedPreferences.getString(
                "color_option",
                "EIGHT"
            ) == "EIGHT" || sharedPreferences.getString(
                "color_option",
                "NINE"
            ) == "NINE" || sharedPreferences.getString("color_option", "TEEN") == "TEEN"
        ) {
            binding!!.toolbar.mode.setBackgroundResource(R.drawable.sun)
        } else if (sharedPreferences.getString(
                "color_option",
                "NIGHT_ONE"
            ) == "NIGHT_ONE" || sharedPreferences.getString(
                "color_option",
                "NIGHT_TWO"
            ) == "NIGHT_TWO" || sharedPreferences.getString(
                "color_option",
                "NIGHT_THREE"
            ) == "NIGHT_THREE" || sharedPreferences.getString(
                "color_option",
                "NIGHT_FOUR"
            ) == "NIGHT_FOUR" || sharedPreferences.getString(
                "color_option",
                "NIGHT_FIVE"
            ) == "NIGHT_FIVE" || sharedPreferences.getString(
                "color_option",
                "NIGHT_SIX"
            ) == "NIGHT_SIX" || sharedPreferences.getString(
                "color_option",
                "NIGHT_SEVEN"
            ) == "NIGHT_SEVEN"
        ) {
            binding!!.toolbar.mode.setBackgroundResource(R.drawable.moon)
        }
        binding!!.toolbar.image.setOnClickListener {
            VOID.IntentExtra(context, CLASS.PROFILE, DATA.PROFILE_ID, DATA.FirebaseUserUid)
        }

        //binding!!.recyclerView.setHasFixedSize(true)
        list = ArrayList()
        adapter = MainAdapter(context, list as ArrayList<Main>)
        binding!!.recyclerView.adapter = adapter
    }

    private fun IdeaPosts(
        users: Int, hotProduct: Int, posts: Int, shoppingCentres: Int, sliderShow: Int
    ) {
        list!!.clear()
        val item1 = Main(R.drawable.ic_person_white, "Users", users - 1, CLASS.USERS)
        val item2 = Main(R.drawable.ic_hot, "Hottest", hotProduct, CLASS.HOT_PRODUCTS)
        val item3 = Main(R.drawable.ic_post, "My Posts", posts, CLASS.POSTS)
        val item4 = Main(R.drawable.ic_add, "Add Post", 0, CLASS.POST_ADD)
        val item5 =
            Main(R.drawable.ic_store, "Shopping Centers", shoppingCentres, CLASS.SHOPPING_CENTRES)
        val item6 = Main(R.drawable.ic_add, "Add Shopping Center", 0, CLASS.SHOPPING_CENTRES_ADD)
        val item7 = Main(R.drawable.ic_rank, "Current Session", 0, CLASS.SESSION_NOW)
        val item8 = Main(R.drawable.ic_rank, "Previous Session", 0, CLASS.SESSION_OLD)
        val item9 = Main(R.drawable.ic_slider, "Slider Show", sliderShow, CLASS.SLIDER_SHOW)
        val item10 = Main(R.drawable.ic_ad, "Ad Monitor", 0, CLASS.ADS_METER)
        val item11 = Main(R.drawable.ic_my, "About Me", 0, CLASS.ABOUT_ME)
        val item12 = Main(R.drawable.ic_settings, "Tools", 0, CLASS.TOOLS)
        list!!.add(item1)
        list!!.add(item2)
        list!!.add(item3)
        list!!.add(item4)
        list!!.add(item5)
        list!!.add(item6)
        list!!.add(item7)
        list!!.add(item8)
        list!!.add(item9)
        list!!.add(item10)
        list!!.add(item11)
        list!!.add(item12)
        adapter!!.notifyDataSetChanged()
        binding!!.progress.visibility = View.GONE
        binding!!.recyclerView.visibility = View.VISIBLE
    }

    private fun userInfo() {
        val reference =
            FirebaseDatabase.getInstance().getReference(DATA.USERS).child(DATA.FirebaseUserUid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!

                VOID.Glide(true, context, user.imageurl, binding!!.toolbar.image)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    var U = 0
    var H = 0
    var P = 0
    var SH = 0
    var S = 0
    private fun nrItems() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.USERS)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                U = 0
                for (data in dataSnapshot.children) {
                    val item = data.getValue(User::class.java)!!
                    if (item.id != null) U++
                }
                nrHotProduct()
            }

            private fun nrHotProduct() {
                val reference = FirebaseDatabase.getInstance().getReference(DATA.HOT_PRODUCT)
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        H = 0
                        H = dataSnapshot.childrenCount.toInt()
                        nrPosts()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            private fun nrPosts() {
                val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        P = 0
                        for (data in dataSnapshot.children) {
                            val item = data.getValue(Post::class.java)!!
                            if (item.publisher == DATA.FirebaseUserUid) P++
                        }
                        nrShoppingCenters()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            private fun nrShoppingCenters() {
                val reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS)
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        SH = 0
                        for (data in dataSnapshot.children) {
                            val item = data.getValue(Post::class.java)!!
                            if (item.publisher == DATA.FirebaseUserUid) SH++
                        }
                        nrSliderShow()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            private fun nrSliderShow() {
                val reference = FirebaseDatabase.getInstance().getReference(DATA.SLIDER_SHOW)
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        S = 0
                        S = dataSnapshot.childrenCount.toInt()
                        IdeaPosts(U, H, P, SH, S)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {}
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    // Color Mode ----------------------------- Start
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "color_option") {
            recreate()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETTINGS_CODE) {
            recreate()
        }
    }

    // Color Mode -------------------------------- End
    override fun onResume() {
        userInfo()
        nrItems()
        super.onResume()
    }

    companion object {
        private const val SETTINGS_CODE = 234
    }
}