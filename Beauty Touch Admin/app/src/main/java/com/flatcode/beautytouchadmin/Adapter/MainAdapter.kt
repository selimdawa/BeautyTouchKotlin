package com.flatcode.beautytouchadmin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.beautytouchadmin.Model.Main
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ItemMainBinding
import java.text.MessageFormat

class MainAdapter(private val context: Context, var list: MutableList<Main>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMainBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        val image = model.image
        val number = model.number
        val name = model.title
        val c = model.c

        if (image != 0) holder.image.setImageResource(image) else holder.image.setImageResource(R.drawable.ic_load)
        if (number != 0) {
            holder.number.visibility = View.VISIBLE
            holder.number.text = MessageFormat.format("{0}{1}", DATA.EMPTY, number)
        } else {
            holder.number.visibility = View.GONE
        }
        holder.name.text = name
        holder.itemView.setOnClickListener { VOID.Intent1(context, c) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        val name: TextView = binding.name
        val number: TextView = binding.number
        val image: ImageView = binding.image
        val item: LinearLayout = binding.item
    }
}
