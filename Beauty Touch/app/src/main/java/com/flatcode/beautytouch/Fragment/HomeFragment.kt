package com.flatcode.beautytouch.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flatcode.beautytouch.Adapter.ImageSliderAdapter
import com.flatcode.beautytouch.Adapter.PostHotAdapter
import com.flatcode.beautytouch.Adapter.PostLinearAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    var hotProductList: MutableList<String?>? = null
    var hotpostAdapter: PostHotAdapter? = null
    var hotpostLists: MutableList<Post?>? = null
    var allpostAdapter: PostLinearAdapter? = null
    var allpostLists: MutableList<Post?>? = null
    var TotalCounts = 0
    var publisher = "KTWe3PaSUSbv3xulRKSwUgConC92"
    var aname = "Beauty Touch"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context), container, false)

        //binding!!.recyclerView.setHasFixedSize(true)
        hotpostLists = ArrayList()
        hotpostAdapter = PostHotAdapter(context, hotpostLists as ArrayList<Post?>)
        binding!!.recyclerView.adapter = hotpostAdapter

        //binding.recyclerView.setHasFixedSize(true);
        allpostLists = ArrayList()
        allpostAdapter = PostLinearAdapter(context, allpostLists as ArrayList<Post?>)
        binding!!.recyclerView2.adapter = allpostAdapter

        FirebaseDatabase.getInstance().getReference("ImageLinks")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val counts = snapshot.childrenCount
                    TotalCounts = counts.toInt()
                    binding!!.imageSlider.sliderAdapter = ImageSliderAdapter(context, TotalCounts)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
        return binding!!.root
    }

    private fun checkHotProduct() {
        hotProductList = ArrayList()
        val reference = FirebaseDatabase.getInstance().getReference(DATA.HOT_PRODUCT)
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
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
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                hotpostLists!!.clear()
                for (snapshot in dataSnapshot.children) {
                    val post = snapshot.getValue(Post::class.java)
                    for (id in hotProductList!!) {
                        assert(post != null)
                        if (post!!.publisher == publisher) if (post.aname == aname) if (post.postid == id) hotpostLists!!.add(
                            post
                        )
                    }
                }
                hotpostAdapter!!.notifyDataSetChanged()
                binding!!.progressCircular.visibility = View.GONE
                binding!!.recyclerView.visibility = View.VISIBLE
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private val showMoreProduct: Unit
        get() {
            val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    allpostLists!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val post = snapshot.getValue(Post::class.java)!!
                        if (post.publisher == publisher) if (post.aname == aname) allpostLists!!.add(
                            post
                        )
                    }
                    allpostAdapter!!.notifyDataSetChanged()
                    binding!!.progressCircular2.visibility = View.GONE
                    binding!!.recyclerView2.visibility = View.VISIBLE
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }

    override fun onResume() {
        checkHotProduct()
        showMoreProduct
        super.onResume()
    }
}