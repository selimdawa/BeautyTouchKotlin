package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.ADsUserAdapter
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.databinding.ActivityAdsMeterBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ADsMeterActivity : AppCompatActivity() {

    private var binding: ActivityAdsMeterBinding? = null
    private val context: Context = this@ADsMeterActivity
    var list: ArrayList<User?>? = null
    var adapter: ADsUserAdapter? = null
    var type: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityAdsMeterBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        type = DATA.AD_LOAD
        binding!!.toolbar.nameSpace.setText(R.string.users_ads)
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = ADsUserAdapter(context, list!!, true)
        binding!!.recyclerView.adapter = adapter

        binding!!.adLoad.setOnClickListener {
            type = DATA.AD_LOAD
            getData(type)
        }
        binding!!.adClick.setOnClickListener {
            type = DATA.AD_CLICK
            getData(type)
        }
        binding!!.timestamp.setOnClickListener {
            type = DATA.STARTED
            getData(type)
        }
    }

    private fun getData(orderBy: String?) {
        val ref: Query = FirebaseDatabase.getInstance().getReference(DATA.USERS)
        ref.orderByChild(orderBy!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list!!.clear()
                for (data in dataSnapshot.children) {
                    val item = data.getValue(User::class.java)!!
                    if (!(item.adLoad == 0 && item.adClick == 0)) list!!.add(item)
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