package com.flatcode.beautytouch.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouch.Adapter.ProductsStaggeredAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.R
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.THEME
import com.flatcode.beautytouch.Unit.VOID.BannerAd
import com.flatcode.beautytouch.databinding.ActivityFavoritesBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FavoritesActivity : AppCompatActivity() {

    private val context: Context = this@FavoritesActivity
    private var binding: ActivityFavoritesBinding? = null
    private var postList: MutableList<Post?>? = null
    private var mySaves: MutableList<String?>? = null
    private var adapter: ProductsStaggeredAdapter? = null
    var publisher = "KTWe3PaSUSbv3xulRKSwUgConC92"
    var aname = "Beauty Touch"

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.favorites)
        BannerAd(applicationContext, binding!!.adView, DATA.BANNER_FAVORITES)

        //binding.recyclerView.setHasFixedSize(true);
        postList = ArrayList()
        adapter = ProductsStaggeredAdapter(context, postList as ArrayList<Post?>)
        binding!!.recyclerView.adapter = adapter
    }

    private fun mySaves() {
        mySaves = ArrayList()
        val reference =
            FirebaseDatabase.getInstance().getReference(DATA.SAVES).child(DATA.FirebaseUserUid)
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
                postList!!.clear()
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)
                    for (id in mySaves!!) {
                        assert(post != null)
                        if (post!!.publisher == publisher) {
                            if (post.aname == aname) {
                                if (post.postid == id)
                                    postList!!.add(post)
                            }
                        }
                    }
                }
                postList!!.reverse()
                binding!!.bar.visibility = View.GONE
                if (postList!!.isNotEmpty()) {
                    binding!!.recyclerView.visibility = View.VISIBLE
                    binding!!.emptyText.visibility = View.GONE
                } else {
                    binding!!.recyclerView.visibility = View.GONE
                    binding!!.emptyText.visibility = View.VISIBLE
                }
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onResume() {
        mySaves()
        super.onResume()
    }

    override fun onRestart() {
        mySaves()
        super.onRestart()
    }
}