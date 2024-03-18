package com.flatcode.beautytouch.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.R
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.Unitimport.CLASS
import com.flatcode.beautytouch.databinding.ItemProductGridBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class ProductsStaggeredAdapter(private val mContext: Context?, private val mPost: List<Post?>) :

    RecyclerView.Adapter<ProductsStaggeredAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemProductGridBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(
            binding!!.root
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = mPost[position]

        if (post != null) {
            VOID.Glide(false, mContext, post.postimage, holder.image_product)
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
                holder.price.text = MessageFormat.format("{0} SYP", post.price)
            }
        }

        isLiked(post!!.postid, holder.like)
        isSaved(post.postid, holder.save)
        nrLikes(holder.likes, post.postid)

        holder.like.setOnClickListener {
            if (holder.like.tag == "like") {
                FirebaseDatabase.getInstance().reference.child(DATA.LIKES).child(post.postid!!)
                    .child(DATA.FirebaseUserUid).setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child(DATA.LIKES).child(post.postid!!)
                    .child(DATA.FirebaseUserUid).removeValue()
            }
        }
        holder.save.setOnClickListener {
            if (holder.save.tag == "save") {
                FirebaseDatabase.getInstance().reference.child(DATA.SAVES)
                    .child(DATA.FirebaseUserUid)
                    .child(post.postid!!).setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child(DATA.SAVES)
                    .child(DATA.FirebaseUserUid)
                    .child(post.postid!!).removeValue()
            }
        }
        holder.card.setOnClickListener {
            VOID.IntentExtra(mContext, CLASS.POST_DETAILS, DATA.POST_ID, post.postid)
        }
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var card: CardView
        var image_product: ImageView
        var like: ImageView
        var save: ImageView
        var likes: TextView
        var name: TextView
        var price: TextView
        var color: LinearLayout

        init {
            card = binding!!.card
            image_product = binding!!.imageProduct
            like = binding!!.like
            name = binding!!.name
            save = binding!!.save
            likes = binding!!.likes
            price = binding!!.price
            color = binding!!.color
        }
    }

    private fun isLiked(postId: String?, imageView: ImageView) {
        val reference = FirebaseDatabase.getInstance().reference.child(DATA.LIKES).child(postId!!)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(DATA.FirebaseUserUid).exists()) {
                    imageView.setImageResource(R.drawable.ic_heart_selected)
                    imageView.tag = "liked"
                } else {
                    imageView.setImageResource(R.drawable.ic_heart_unselected)
                    imageView.tag = "like"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun isSaved(postId: String?, imageView: ImageView) {
        val reference = FirebaseDatabase.getInstance().reference
            .child(DATA.SAVES).child(DATA.FirebaseUserUid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(postId!!).exists()) {
                    imageView.setImageResource(R.drawable.ic_favorites_selected)
                    imageView.tag = "saved"
                } else {
                    imageView.setImageResource(R.drawable.ic_favorites_unselected)
                    imageView.tag = "save"
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
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
        private var binding: ItemProductGridBinding? = null
    }
}