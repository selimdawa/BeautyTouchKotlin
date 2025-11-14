package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.LeaderboardAdapter
import com.flatcode.beautytouchadmin.Model.Tools
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.databinding.ActivitySessionNowInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class SessionNowInfoActivity : AppCompatActivity() {

    private var binding: ActivitySessionNowInfoBinding? = null
    private val context: Context = this@SessionNowInfoActivity
    private var list: ArrayList<User?>? = null
    private var adapter: LeaderboardAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivitySessionNowInfoBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.text = "Session Now"
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = LeaderboardAdapter(context, list!!, true)
        binding!!.recyclerView.adapter = adapter
    }

    private fun SessionInfo() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools = dataSnapshot.getValue(Tools::class.java)!!
                val year = tools.year
                val session = tools.session
                val key = year + "_" + session
                getData(key)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getData(orderBy: String) {
        val ref: Query = FirebaseDatabase.getInstance().getReference(DATA.USERS)
        ref.orderByChild(orderBy).limitToLast(3)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    list!!.clear()
                    for (data in dataSnapshot.children) {
                        if (data.child(orderBy).exists()) {
                            val item = data.getValue(User::class.java)!!
                            list!!.add(item)
                        }
                    }
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
        SessionInfo()
        super.onRestart()
    }

    override fun onResume() {
        SessionInfo()
        super.onResume()
    }
}