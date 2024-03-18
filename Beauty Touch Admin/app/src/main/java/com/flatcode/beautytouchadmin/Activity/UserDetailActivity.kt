package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.FavoritesAdapter
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityUserDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDetailActivity : AppCompatActivity() {

    private var binding: ActivityUserDetailBinding? = null
    private val context: Context = this@UserDetailActivity
    private var list: MutableList<Post?>? = null
    private var mySaves: MutableList<String?>? = null
    private var adapter: FavoritesAdapter? = null
    var profileId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val intent = intent
        profileId = intent.getStringExtra(DATA.PROFILE_ID)

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = FavoritesAdapter(context, list as ArrayList<Post?>)
        binding!!.recyclerView.adapter = adapter
    }

    private fun Saves() {
        mySaves = ArrayList()
        val reference = FirebaseDatabase.getInstance().getReference(DATA.SAVES).child(profileId!!)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (mySaves as ArrayList<String?>).clear()
                for (snapshot in dataSnapshot.children) {
                    (mySaves as ArrayList<String?>).add(snapshot.key)
                }
                readSaves()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun readSaves() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list!!.clear()
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)
                    for (id in mySaves!!) {
                        if (post!!.publisher == DATA.PUBLICHER) {
                            if (post.aname == DATA.APP_NAME) {
                                if (post.postid == id) {
                                    list!!.add(post)
                                }
                            }
                        }
                    }
                }
                list!!.reverse()
                adapter!!.notifyDataSetChanged()
                binding!!.recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun userInfo() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(profileId!!)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)!!

                VOID.Glide(true, context, user.imageurl, binding!!.image)
                binding!!.name.text = user.username
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onRestart() {
        Saves()
        userInfo()
        super.onRestart()
    }

    override fun onResume() {
        Saves()
        userInfo()
        super.onResume()
    }
}