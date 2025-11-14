package com.flatcode.beautytouch.Activity

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.flatcode.beautytouch.Adapter.LeaderboardOldAdapter
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.Model.Tools
import com.flatcode.beautytouch.Model.User
import com.flatcode.beautytouch.Modelimport.Reward
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.THEME
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.Unitimport.CLASS
import com.flatcode.beautytouch.databinding.ActivityLeaderboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class LeaderboardOldActivity : AppCompatActivity() {

    private var binding: ActivityLeaderboardBinding? = null
    var context: Context = this@LeaderboardOldActivity
    private var list: ArrayList<User?>? = null
    private var adapter: LeaderboardOldAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        THEME.setThemeOfApp(context)
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        val view = binding!!.root
        setContentView(view)

        //binding.recyclerView.setHasFixedSize(true);
        list = ArrayList()
        adapter = LeaderboardOldAdapter(context, list!!)
        binding!!.recyclerView.adapter = adapter
    }

    private fun SessionInfo() {
        val reference: DatabaseReference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools: Tools = dataSnapshot.getValue(Tools::class.java)!!
                val year: String = tools.oldYear!!
                val session: String = tools.oldSession!!
                val sessionNumber: String = tools.oldSessionNumber!!

                VOID.Glide(false, context, tools.oldImageSession, binding!!.imageSession)
                VOID.Glide(false, context, tools.oldImageLogo, binding!!.imageLogo)
                binding!!.sessionNumber.text = session
                val key = year + "_" + sessionNumber

                getData(key)
                Reward(key)
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
                            val item: User = data.getValue(User::class.java)!!
                            list!!.add(item)
                        }
                    }
                    adapter!!.notifyDataSetChanged()
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    private fun Reward(key: String) {
        val reference: DatabaseReference =
            FirebaseDatabase.getInstance().getReference(DATA.M_REWARD)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val reward: Reward = dataSnapshot.getValue(Reward::class.java)!!
                if (dataSnapshot.child(key).exists()) {
                    if (dataSnapshot.exists()) {
                        if (reward.reward != null) ReadReward(
                            reward.reward!!, binding!!.reward
                        )
                        if (reward.reward2 != null) ReadReward(
                            reward.reward2!!, binding!!.reward2
                        )
                        if (reward.reward3 != null) ReadReward(
                            reward.reward3!!, binding!!.reward3
                        )
                        if (reward.reward4 != null) ReadReward(
                            reward.reward4!!, binding!!.reward4
                        )
                        if (reward.reward5 != null) ReadReward(
                            reward.reward5!!, binding!!.reward5
                        )
                        if (reward.reward6 != null) ReadReward(
                            reward.reward6!!, binding!!.reward6
                        )
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun ReadReward(R: String, Reward: ImageView) {
        if (R != DATA.EMPTY) {
            val reference: DatabaseReference =
                FirebaseDatabase.getInstance().getReference(DATA.POSTS).child(R)
            reference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val post: Post = dataSnapshot.getValue(Post::class.java)!!
                    if (post.postid == R) {
                        VOID.Glide(false, context, post.postimage, Reward)
                        Reward.setOnClickListener {
                            VOID.IntentExtra(context, CLASS.POST_DETAILS, DATA.POST_ID, R)
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
        }
    }

    override fun onResume() {
        SessionInfo()
        super.onResume()
    }

    override fun onRestart() {
        SessionInfo()
        super.onRestart()
    }
}