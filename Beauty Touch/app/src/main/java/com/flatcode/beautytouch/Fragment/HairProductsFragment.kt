package com.flatcode.beautytouch.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flatcode.beautytouch.Adapter.ProductsStaggeredAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.VOID.BannerAd
import com.flatcode.beautytouch.databinding.FragmentHairProductsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HairProductsFragment : Fragment() {

    private var binding: FragmentHairProductsBinding? = null
    private var list: MutableList<Post?>? = null
    private var adapter: ProductsStaggeredAdapter? = null
    var publisher = "KTWe3PaSUSbv3xulRKSwUgConC92"
    var aname = "Beauty Touch"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            FragmentHairProductsBinding.inflate(LayoutInflater.from(context), container, false)

        BannerAd(context, binding!!.adView, DATA.BANNER_HAIR)

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = ProductsStaggeredAdapter(context, list as ArrayList<Post?>)
        binding!!.recyclerView.adapter = adapter

        return binding!!.root
    }

    private val allPosts: Unit
        get() {
            val reference = FirebaseDatabase.getInstance().getReference(DATA.POSTS)
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    list!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val post = snapshot.getValue(Post::class.java)!!
                        if (post.category == DATA.HAIR_PRODUCTS) {
                            if (post.publisher == publisher) {
                                if (post.aname == aname) {
                                    list!!.add(post)
                                }
                            }
                        }
                    }
                    list!!.reverse()
                    binding!!.bar.visibility = View.GONE
                    if (list!!.isNotEmpty()) {
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
        allPosts
        super.onResume()
    }
}