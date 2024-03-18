package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.PostDetailAdapter
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.databinding.ActivityPostDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostDetailsActivity : AppCompatActivity() {

    var context: Context = this@PostDetailsActivity
    private var binding: ActivityPostDetailsBinding? = null
    private var adapter: PostDetailAdapter? = null
    private var list: MutableList<Post?>? = null
    var postId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailsBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        val intent = intent
        postId = intent.getStringExtra(DATA.POST_ID)

        binding!!.toolbar.nameSpace.setText(R.string.post_detail)
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = PostDetailAdapter(context, list as ArrayList<Post?>)
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

    //private void addView() {
    //    FirebaseDatabase.getInstance().getReference(DATA.POSTS).child(postId).child("views")
    //            .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(true);
    //}
    override fun onRestart() {
        readPost()
        //addView();
        super.onRestart()
    }

    override fun onResume() {
        readPost()
        //addView();
        super.onResume()
    }
}