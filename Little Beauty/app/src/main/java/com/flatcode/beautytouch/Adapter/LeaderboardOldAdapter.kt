package com.flatcode.beautytouch.Adapter

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
import com.flatcode.beautytouch.Filter.LeaderboardOldFilter
import com.flatcode.beautytouch.Model.Tools
import com.flatcode.beautytouch.Model.User
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.databinding.ItemLeaderboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class LeaderboardOldAdapter(private val mContext: Context, var list: ArrayList<User?>) :
    RecyclerView.Adapter<LeaderboardOldAdapter.ViewHolder>(), Filterable {

    var filterList: ArrayList<User?>
    private var filter: LeaderboardOldFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        val id = user!!.id
        val username = user.username
        val image = user.imageurl
        val First = holder.position
        val Final = list.size - First

        holder.range.text = MessageFormat.format("{0}", Final)
        VOID.Glide(true, mContext, image, holder.image_profile)

        if (username == DATA.EMPTY) {
            holder.username.visibility = View.GONE
        } else {
            holder.username.visibility = View.VISIBLE
            holder.username.text = username
        }
        SessionInfo(holder.points, id)
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

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var image_profile: ImageView
        var username: TextView
        var range: TextView
        var points: TextView
        var linear_one: LinearLayout

        init {
            username = binding!!.username
            image_profile = binding!!.imageProfile
            range = binding!!.range
            points = binding!!.points
            linear_one = binding!!.linearOne
        }
    }

    private fun SessionInfo(points: TextView, id: String?) {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.M_TOOLS)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val tools = dataSnapshot.getValue(Tools::class.java)!!
                val year = tools.oldYear
                val session = tools.oldSessionNumber
                Points(year, session, points, id)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun Points(year: String?, session: String?, points: TextView, id: String?) {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.USERS).child(id!!)
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

    companion object {
        private var binding: ItemLeaderboardBinding? = null
    }

    init {
        filterList = list
    }
}