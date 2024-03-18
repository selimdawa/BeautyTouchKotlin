package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.ShoppingCentersAdapter
import com.flatcode.beautytouchadmin.Model.ShoppingCenter
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.databinding.ActivityShoppingCentersBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShoppingCentersActivity : AppCompatActivity() {

    private var binding: ActivityShoppingCentersBinding? = null
    private val context: Context = this@ShoppingCentersActivity
    private var list: MutableList<ShoppingCenter?>? = null
    private var adapter: ShoppingCentersAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingCentersBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.shopping_centers)
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = ShoppingCentersAdapter(context, list as ArrayList<ShoppingCenter?>)
        binding!!.recyclerView.adapter = adapter
    }

    private val allPosts: Unit
        get() {
            val reference = FirebaseDatabase.getInstance().getReference(DATA.SHOPPING_CENTERS)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    list!!.clear()
                    for (snapshot in dataSnapshot.children) {
                        val shoppingCenter = snapshot.getValue(ShoppingCenter::class.java)!!
                        if (shoppingCenter.publisher == DATA.PUBLICHER) {
                            if (shoppingCenter.aname == DATA.APP_NAME) {
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

    override fun onRestart() {
        allPosts
        super.onRestart()
    }

    override fun onResume() {
        allPosts
        super.onResume()
    }
}