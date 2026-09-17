package com.flatcode.beautytouchadmin.Filter

import android.widget.Filter
import com.flatcode.beautytouchadmin.Adapter.ADsInfoAdapter
import com.flatcode.beautytouchadmin.Model.ADs
import java.util.*

class ADsInfoFilter(var list: MutableList<ADs?>, var adapter: ADsInfoAdapter) : Filter() {
    override fun performFiltering(constraint: CharSequence): FilterResults {
        var constraintStr: CharSequence? = constraint
        val results = FilterResults()
        if (constraintStr != null && constraintStr.isNotEmpty()) {
            constraintStr = constraintStr.toString().uppercase(Locale.getDefault())
            val filter = mutableListOf<ADs?>()
            for (i in list.indices) {
                if (list[i]!!.name!!.uppercase(Locale.getDefault()).contains(constraintStr)) {
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
        adapter.list = (results.values as MutableList<ADs?>)
        adapter.notifyDataSetChanged()
    }
}
