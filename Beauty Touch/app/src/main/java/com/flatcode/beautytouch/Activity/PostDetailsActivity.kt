package com.flatcode.beautytouch.Activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouch.Adapter.PostDetailAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.R
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.THEME
import com.flatcode.beautytouch.databinding.ActivityPostDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Objects

class PostDetailsActivity : AppCompatActivity() {

    var context: Context = this@PostDetailsActivity
    private var binding: ActivityPostDetailBinding? = null
    private var adapter: PostDetailAdapter? = null
    private var list: MutableList<Post?>? = null
    var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val intent = intent
        postId = intent.getStringExtra(DATA.POST_ID)
        binding!!.toolbar.nameSpace.setText(R.string.post_detail)

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = PostDetailAdapter(context, list)
        binding!!.recyclerView.adapter = adapter
    }

    private fun readPost() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS).child(postId!!)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list!!.clear()
                val post = dataSnapshot.getValue(Post::class.java)
                list!!.add(post)
                adapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun addView() {
        FirebaseDatabase.getInstance().getReference(DATA.POSTS).child(postId!!).child("views")
            .child(Objects.requireNonNull(FirebaseAuth.getInstance().currentUser)!!.uid)
            .setValue(true)
    }

    override fun onResume() {
        readPost()
        addView()
        super.onResume()
    }

    override fun onRestart() {
        readPost()
        addView()
        super.onRestart()
    }
}