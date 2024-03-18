package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.LeaderboardOldAdapter
import com.flatcode.beautytouchadmin.Model.Tools
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.databinding.ActivitySessionOldInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class SessionOldInfoActivity : AppCompatActivity() {

    private var binding: ActivitySessionOldInfoBinding? = null
    private val context: Context = this@SessionOldInfoActivity
    private var list: ArrayList<User?>? = null
    private var adapter: LeaderboardOldAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivitySessionOldInfoBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.text = "Session Old"
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = LeaderboardOldAdapter(context, list!!, true)
        binding!!.recyclerView.adapter = adapter
    }

    private fun SessionInfo() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools = dataSnapshot.getValue(Tools::class.java)!!
                val year = tools.oldYear
                val session = tools.oldSession
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