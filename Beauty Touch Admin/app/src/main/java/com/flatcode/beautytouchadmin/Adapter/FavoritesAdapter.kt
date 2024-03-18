package com.flatcode.beautytouchadmin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.beautytouchadmin.Model.Post
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ItemProductLinearBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class FavoritesAdapter(private val mContext: Context, private val mPost: List<Post?>) :
    RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemProductLinearBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = mPost[position]
        val id = DATA.EMPTY + post!!.postid

        VOID.Glide(true, mContext, post.postimage, holder.image_product)
        if (post.name == DATA.EMPTY) {
            holder.name.visibility = View.GONE
        } else {
            holder.name.visibility = View.VISIBLE
            holder.name.text = post.name
        }
        if (post.price == DATA.EMPTY) {
            holder.price.visibility = View.GONE
        } else {
            holder.price.visibility = View.VISIBLE
            holder.price.text = MessageFormat.format("{0} $", post.price)
        }

        nrLikes(holder.likes, post.postid)
        holder.card.setOnClickListener {
            VOID.IntentExtra(mContext, CLASS.POST_DETAILS, DATA.POST_ID, id)
        }
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var card: CardView
        var image_product: ImageView
        var like: ImageView
        var likes: TextView
        var name: TextView
        var price: TextView
        var save: ImageView

        init {
            card = binding!!.card
            image_product = binding!!.imageProduct
            like = binding!!.like
            likes = binding!!.likes
            save = binding!!.save
            name = binding!!.name
            price = binding!!.price
        }
    }

    private fun nrLikes(likes: TextView, postId: String?) {
        val reference = FirebaseDatabase.getInstance().reference.child(DATA.LIKES).child(postId!!)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                likes.text = MessageFormat.format("{0}", dataSnapshot.childrenCount)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    companion object {
        private var binding: ItemProductLinearBinding? = null
    }
}