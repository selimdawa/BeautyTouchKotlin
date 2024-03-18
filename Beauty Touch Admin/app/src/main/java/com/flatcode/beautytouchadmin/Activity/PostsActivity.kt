package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.MyPostsAdapter
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.databinding.ActivityPostsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostsActivity : AppCompatActivity() {

    private var binding: ActivityPostsBinding? = null
    private val context: Context = this@PostsActivity
    var adapter: MyPostsAdapter? = null
    var list: MutableList<Post?>? = null
    var type = DATA.ALL

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityPostsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.text = "My Posts"
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = MyPostsAdapter(context, list as ArrayList<Post?>)
        binding!!.recyclerView.adapter = adapter

        binding!!.all.setOnClickListener {
            type = DATA.ALL
            getData(type)
        }
        binding!!.hair.setOnClickListener {
            type = DATA.HAIR
            getData(type)
        }
        binding!!.skin.setOnClickListener {
            type = DATA.SKIN
            getData(type)
        }
    }

    private fun getData(type: String) {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list!!.clear()
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)!!
                    if (post.aname == DATA.BEAUTY_TOUCH) {
                        when (type) {
                            DATA.ALL -> list!!.add(post)
                            DATA.SKIN -> if (post.category == DATA.SKIN_PRODUCTS) list!!.add(post)
                            DATA.HAIR -> if (post.category == DATA.HAIR_PRODUCTS) list!!.add(post)
                        }
                    }
                }
                adapter!!.notifyDataSetChanged()
                binding!!.progress.visibility = View.GONE
                if (list!!.isNotEmpty()) {
                    binding!!.recyclerView.visibility = View.VISIBLE
                    binding!!.emptyText.visibility = View.GONE
                } else {
                    binding!!.recyclerView.visibility = View.GONE
                    binding!!.emptyText.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    override fun onRestart() {
        getData(type)
        super.onRestart()
    }

    override fun onResume() {
        getData(type)
        super.onResume()
    }
}