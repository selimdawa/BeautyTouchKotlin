package com.flatcode.beautytouch.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.flatcode.beautytouch.Adapter.ShoppingCentersAdapter
import com.flatcode.beautytouch.Model.ShoppingCenter
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.databinding.FragmentShoppingCentersBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShoppingCentersFragment : Fragment() {

    private var binding: FragmentShoppingCentersBinding? = null
    private var list: MutableList<ShoppingCenter?>? = null
    private var adapter: ShoppingCentersAdapter? = null
    var publisher = "KTWe3PaSUSbv3xulRKSwUgConC92"
    var aname = "Beauty Touch"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            FragmentShoppingCentersBinding.inflate(LayoutInflater.from(context), container, false)

        VOID.BannerAd(context, binding!!.adView, DATA.BANNER_SHOPPING_CENTRES)

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = ShoppingCentersAdapter(context, list as ArrayList<ShoppingCenter?>)
        binding!!.recyclerView.adapter = adapter

        return binding!!.root
    }

    private val allPosts: Unit
        get() {
            val reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS)
            reference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    list!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val shoppingCenter = snapshot.getValue(ShoppingCenter::class.java)!!
                        if (shoppingCenter.publisher == publisher) {
                            if (shoppingCenter.aname == aname) {
                                list!!.add(shoppingCenter)
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