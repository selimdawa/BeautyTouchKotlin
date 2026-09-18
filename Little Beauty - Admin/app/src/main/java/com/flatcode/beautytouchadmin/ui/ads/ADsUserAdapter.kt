package com.flatcode.beautytouchadmin.ui.ads

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
import com.flatcode.beautytouchadmin.Application
import com.flatcode.beautytouchadmin.filter.ADsUserFilter
import com.flatcode.beautytouchadmin.model.User
import com.flatcode.beautytouchadmin.utils.CLASS
import com.flatcode.beautytouchadmin.utils.DATA
import com.flatcode.beautytouchadmin.utils.VOID
import com.flatcode.beautytouchadmin.databinding.ItemAdsUserBinding
import java.text.MessageFormat

class ADsUserAdapter(private val context: Context, var list: MutableList<User?>, var isUser: Boolean) :
    RecyclerView.Adapter<ADsUserAdapter.ViewHolder>(), Filterable {

    var filterList: MutableList<User?> = list
    private var filter: ADsUserFilter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdsUserBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val userId = DATA.EMPTY + item!!.id
        val username = DATA.EMPTY + item.username
        val profileImage = DATA.EMPTY + item.imageurl
        val timestamp = DATA.EMPTY + item.started
        val adLoaded = DATA.EMPTY + item.adLoad
        val adClicked = DATA.EMPTY + item.adClick
        val formattedDate: String = Application.formatTimestamp(timestamp.toLong())

        VOID.Glide(true, context, profileImage, holder.profileImage)
        if (username == DATA.EMPTY) {
            holder.username.visibility = View.GONE
        } else {
            holder.username.visibility = View.VISIBLE
            holder.username.text = username
        }

        val rankValue = list.size - position
        holder.time.text = formattedDate
        holder.rank.text = MessageFormat.format("{0}", rankValue)
        holder.numberADsLoad.text = MessageFormat.format("{0}{1}", DATA.EMPTY, adLoaded)
        holder.numberADsClick.text = MessageFormat.format("{0}{1}", DATA.EMPTY, adClicked)

        holder.item.setOnClickListener {
            VOID.IntentExtra(context, CLASS.ADS_INFO, DATA.PROFILE_ID, userId)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = ADsUserFilter(filterList as ArrayList<User?>, this)
        }
        return filter!!
    }

    class ViewHolder(binding: ItemAdsUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val profileImage: ImageView = binding.profileImage
        val username: TextView = binding.username
        val rank: TextView = binding.rank
        val numberADsLoad: TextView = binding.numberADsLoad
        val numberADsClick: TextView = binding.numberADsClick
        val time: TextView = binding.time
        val item: LinearLayout = binding.item
    }
}
