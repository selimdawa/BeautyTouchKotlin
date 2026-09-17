package com.flatcode.beautytouch.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.beautytouch.Model.ShoppingCenter
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.databinding.ItemShoppingCentersBinding
import java.text.MessageFormat

class ShoppingCentersAdapter(
    private val mContext: Context?, private val mShoppingCenters: List<ShoppingCenter?>
) : RecyclerView.Adapter<ShoppingCentersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemShoppingCentersBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shoppingCenter = mShoppingCenters[position]

        if (shoppingCenter != null) {
            VOID.Glide(false, mContext, shoppingCenter.imageurl, holder.image_product)
            VOID.Glide(false, mContext, shoppingCenter.imageurl2, holder.image_product2)
            if (shoppingCenter.name == DATA.EMPTY) {
                holder.linearName.visibility = View.GONE
            } else {
                holder.linearName.visibility = View.VISIBLE
                holder.name.text = shoppingCenter.name
            }
            if (shoppingCenter.location == DATA.EMPTY && shoppingCenter.location2 == DATA.EMPTY) {
                holder.linearLocation.visibility = View.GONE
                holder.view.visibility = View.GONE
            } else {
                holder.linearLocation.visibility = View.VISIBLE
                holder.view.visibility = View.VISIBLE
                holder.location.text = MessageFormat.format(
                    "{0} - {1} - {2}",
                    shoppingCenter.location,
                    shoppingCenter.location2,
                    shoppingCenter.location3
                )
            }

            if (shoppingCenter.numberPhone == DATA.EMPTY) {
                holder.linearNumberPhone.visibility = View.GONE
                holder.view2.visibility = View.GONE
            } else {
                holder.linearNumberPhone.visibility = View.VISIBLE
                holder.view2.visibility = View.VISIBLE
                holder.numberPhone.text = shoppingCenter.numberPhone
            }
        }
    }

    override fun getItemCount(): Int {
        return mShoppingCenters.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var image_product: ImageView
        var image_product2: ImageView
        var name: TextView
        var location: TextView
        var numberPhone: TextView
        var linearName: LinearLayout
        var linearLocation: LinearLayout
        var linearNumberPhone: LinearLayout
        var view: View
        var view2: View

        init {
            image_product = binding!!.imageProduct
            image_product2 = binding!!.imageProduct2
            name = binding!!.name
            location = binding!!.location
            numberPhone = binding!!.numberPhone
            linearName = binding!!.linearName
            linearLocation = binding!!.linearLocation
            linearNumberPhone = binding!!.linearNumberPhone
            view = binding!!.view
            view2 = binding!!.view2
        }
    }

    companion object {
        private var binding: ItemShoppingCentersBinding? = null
    }
}