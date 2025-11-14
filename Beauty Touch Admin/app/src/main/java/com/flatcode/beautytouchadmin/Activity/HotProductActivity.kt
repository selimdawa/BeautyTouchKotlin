package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.HotProductAddAdapter
import com.flatcode.beautytouchadmin.Adapter.HotProductRemoveAdapter
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.databinding.ActivityHotProductBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HotProductActivity : AppCompatActivity() {

    private var binding: ActivityHotProductBinding? = null
    private val context: Context = this@HotProductActivity
    var hotProductList: MutableList<String?>? = null
    var hotpostAdapter: HotProductRemoveAdapter? = null
    var hotpostLists: MutableList<Post?>? = null
    var allpostAdapter: HotProductAddAdapter? = null
    var allpostLists: MutableList<Post?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityHotProductBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.hot_product)
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        //binding.recyclerView.setHasFixedSize(true);
        hotpostLists = ArrayList()
        hotpostAdapter = HotProductRemoveAdapter(context, hotpostLists as ArrayList<Post?>)
        binding!!.recyclerView.adapter = hotpostAdapter

        //binding.recyclerView2.setHasFixedSize(true);
        allpostLists = ArrayList()
        allpostAdapter = HotProductAddAdapter(context, allpostLists as ArrayList<Post?>)
        binding!!.recyclerView2.adapter = allpostAdapter
    }

    private fun checkHotProduct() {
        hotProductList = ArrayList()
        val reference = FirebaseDatabase.getInstance().getReference(DATA.HOT_PRODUCT)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                (hotProductList as ArrayList<String?>).clear()
                for (snapshot in dataSnapshot.children) {
                    (hotProductList as ArrayList<String?>).add(snapshot.key)
                }
                readPosts()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun readPosts() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hotpostLists!!.clear()
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)
                    for (id in hotProductList!!) {
                        assert(post != null)
                        if (post!!.publisher == DATA.PUBLICHER) if (post.aname == DATA.APP_NAME)
                            if (post.postid == id) hotpostLists!!.add(
                                post
                            )
                    }
                }
                hotpostLists!!.reverse()
                hotpostAdapter!!.notifyDataSetChanged()
                binding!!.progressBar.visibility = View.GONE
                binding!!.recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private val moreProduct: Unit
        get() {
            val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    allpostLists!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val post = snapshot.getValue(Post::class.java)!!
                        if (post.publisher == DATA.PUBLICHER) {
                            if (post.aname == DATA.APP_NAME) {
                                allpostLists!!.add(post)
                                for (id in hotProductList!!) {
                                    if (post.postid == id) {
                                        allpostLists!!.remove(post)
                                    }
                                }
                            }
                        }
                    }
                    allpostLists!!.reverse()
                    allpostAdapter!!.notifyDataSetChanged()
                    binding!!.progressBar2.visibility = View.GONE
                    binding!!.recyclerView2.visibility = View.VISIBLE
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    override fun onRestart() {
        checkHotProduct()
        moreProduct
        super.onRestart()
    }

    override fun onResume() {
        checkHotProduct()
        moreProduct
        super.onResume()
    }
}