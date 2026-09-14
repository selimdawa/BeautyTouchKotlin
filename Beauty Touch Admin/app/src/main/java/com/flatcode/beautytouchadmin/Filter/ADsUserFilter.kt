package com.flatcode.beautytouchadmin.Filter

import android.widget.Filter
import com.flatcode.beautytouchadmin.Adapter.ADsUserAdapter
import com.flatcode.beautytouchadmin.Model.User
import java.util.*

class ADsUserFilter(var list: MutableList<User?>, var adapter: ADsUserAdapter) : Filter() {
    override fun performFiltering(constraint: CharSequence): FilterResults {
        var constraintStr: CharSequence? = constraint
        val results = FilterResults()
        if (constraintStr != null && constraintStr.isNotEmpty()) {
            constraintStr = constraintStr.toString().uppercase(Locale.getDefault())
            val filter = mutableListOf<User?>()
            for (i in list.indices) {
                if (list[i]!!.username!!.uppercase(Locale.getDefault()).contains(constraintStr)) {
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
        adapter.list = (results.values as MutableList<User?>)
        adapter.notifyDataSetChanged()
    }
}
