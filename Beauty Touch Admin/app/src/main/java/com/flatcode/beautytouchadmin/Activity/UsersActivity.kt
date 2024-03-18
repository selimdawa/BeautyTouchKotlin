package com.flatcode.beautytouchadmin.Activity

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouchadmin.Adapter.UsersAdapter
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.THEME
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ActivityUsersBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UsersActivity : AppCompatActivity() {

    private var binding: ActivityUsersBinding? = null
    private val context: Context = this@UsersActivity
    var adapter: UsersAdapter? = null
    var list: MutableList<User?>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityUsersBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        binding!!.toolbar.nameSpace.setText(R.string.users)
        binding!!.toolbar.back.setOnClickListener { onBackPressed() }

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = UsersAdapter(context, list as ArrayList<User?>)
        binding!!.recyclerView.adapter = adapter
    }

    private fun Users() {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.USERS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list!!.clear()
                for (snapshot in dataSnapshot.children) {
                    val user = snapshot.getValue(User::class.java)!!
                    if (user.id != DATA.FirebaseUserUid) {
                        list!!.add(user)
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
        binding!!.toolbar.nameSpace.setText(R.string.users)
        VOID.getNrFromServer(DATA.USERS, binding!!.toolbar.nameSpace)
        Users()
        super.onResume()
    }
}