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
import com.flatcode.beautytouchadmin.R
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ItemMyPostBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class MyPostsAdapter(private val mContext: Context, private val mPost: List<Post?>) :
    RecyclerView.Adapter<MyPostsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMyPostBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = mPost[position]

        VOID.Glide(false, mContext, post!!.postimage, holder.image_product)
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
        isLiked(post.postid, holder.like)
        holder.like.setOnClickListener {
            if (holder.like.tag == "like") {
                FirebaseDatabase.getInstance().reference.child(DATA.LIKES).child(post.postid!!)
                    .child(DATA.FirebaseUserUid).setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child(DATA.LIKES).child(post.postid!!)
                    .child(DATA.FirebaseUserUid).removeValue()
            }
        }

        holder.more.setOnClickListener { VOID.moreOptionDialog(mContext, post) }
        holder.card.setOnClickListener {
            VOID.IntentExtra(mContext, CLASS.POST_DETAILS, DATA.POST_ID, post.postid)
        }
    }

    override fun getItemCount(): Int {
        return mPost.size
    }

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var image_product: ImageView
        var more: ImageView
        var like: ImageView
        var likes: TextView
        var name: TextView
        var price: TextView
        var card: CardView

        init {
            image_product = binding!!.imageProduct
            more = binding!!.more
            like = binding!!.like
            name = binding!!.name
            price = binding!!.price
            likes = binding!!.likes
            card = binding!!.card
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

    private fun isLiked(postId: String?, imageView: ImageView) {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.LIKES).child(postId!!)
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

    companion object {
        private var binding: ItemMyPostBinding? = null
    }
}