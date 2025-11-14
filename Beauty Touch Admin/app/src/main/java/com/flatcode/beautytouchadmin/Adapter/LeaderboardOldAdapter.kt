package com.flatcode.beautytouchadmin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.beautytouchadmin.Filter.LeaderboardOldFilter
import com.flatcode.beautytouchadmin.Model.Tools
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ItemLeaderboradBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class LeaderboardOldAdapter(
    private val context: Context, var list: ArrayList<User?>, isUser: Boolean
) : RecyclerView.Adapter<LeaderboardOldAdapter.ViewHolder>(), Filterable {

    private var binding: ItemLeaderboradBinding? = null
    var filterList: ArrayList<User?>
    private var filter: LeaderboardOldFilter? = null
    var isUser: Boolean

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemLeaderboradBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val id = DATA.EMPTY + item!!.id
        val username = DATA.EMPTY + item.username
        val image = DATA.EMPTY + item.imageurl
        val First = holder.position
        val Final = list.size - First

        holder.rank.text = MessageFormat.format("{0}", Final)
        VOID.Glide(true, context, image, holder.profileImage)
        if (username == DATA.EMPTY) {
            holder.username.visibility = View.GONE
        } else {
            holder.username.visibility = View.VISIBLE
            holder.username.text = username
        }
        if (username == DATA.EMPTY) {
            holder.username.visibility = View.GONE
        } else {
            holder.username.visibility = View.VISIBLE
            holder.username.text = username
        }

        SessionInfo(holder.numberADsLoad, id)
        holder.item.setOnClickListener {
            VOID.IntentExtra(context, CLASS.ADS_INFO, DATA.PROFILE_ID, id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = LeaderboardOldFilter(filterList, this)
        }
        return filter!!
    }

    inner class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var profileImage: ImageView
        var username: TextView
        var numberADsLoad: TextView
        var rank: TextView
        var item: LinearLayout

        init {
            profileImage = binding!!.profileImage
            username = binding!!.username
            numberADsLoad = binding!!.numberADsLoad
            rank = binding!!.rank
            item = binding!!.item
        }
    }

    private fun SessionInfo(points: TextView, id: String) {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools = dataSnapshot.getValue(Tools::class.java)!!
                val year = tools.oldYear
                val session = tools.oldSession
                Points(year, session, points, id)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun Points(year: String?, session: String?, points: TextView, id: String) {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(id)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val key = year + "_" + session
                val value = DATA.EMPTY + dataSnapshot.child(key).value
                if (dataSnapshot.child(key).exists()) points.text =
                    MessageFormat.format("{0}", value) else points.text = "0"
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    init {
        filterList = list
        this.isUser = isUser
    }
}