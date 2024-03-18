package com.flatcode.beautytouchadmin.Filter

import android.widget.Filter
import com.flatcode.beautytouchadmin.Adapter.LeaderboardOldAdapter
import com.flatcode.beautytouchadmin.Model.User
import java.util.Locale

class LeaderboardOldFilter(var list: ArrayList<User?>, var adapter: LeaderboardOldAdapter) :
    Filter() {
    override fun performFiltering(constraint: CharSequence): FilterResults {
        var constraint: CharSequence? = constraint
        val results = FilterResults()
        if (constraint != null && constraint.length > 0) {
            constraint = constraint.toString().uppercase(Locale.getDefault())
            val filter = ArrayList<User?>()
            for (i in list.indices) {
                if (list[i]!!.username!!.contains(constraint)) {
                    filter.add(list[i])
                }
            }
            results.count = filter.size
            results.values = filter
        } else {
            results.count = list.size
            results.values = list
        }
        return results
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        adapter.list = (results.values as ArrayList<User?>)
        adapter.notifyDataSetChanged()
    }
}