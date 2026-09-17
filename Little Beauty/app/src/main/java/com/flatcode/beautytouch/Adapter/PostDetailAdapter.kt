package com.flatcode.beautytouch.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.beautytouch.Model.Post
import com.flatcode.beautytouch.R
import com.flatcode.beautytouch.Unit.DATA
import com.flatcode.beautytouch.Unit.VOID
import com.flatcode.beautytouch.databinding.ItemPostDetailBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class PostDetailAdapter(private val mContext: Context, private val mPost: MutableList<Post?>?) :
    RecyclerView.Adapter<PostDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostDetailBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = mPost!![position]

        VOID.Glide(false, mContext, post!!.postimage, holder.image_product)
        VOID.Glide(false, mContext, post.postimage, holder.image_product_1)
        VOID.Glide(false, mContext, post.postimage2, holder.image_product_2)
        VOID.Glide(false, mContext, post.postimage3, holder.image_product_3)
        VOID.Glide(false, mContext, post.postimage4, holder.image_product_4)
        VOID.Glide(false, mContext, post.postimage5, holder.image_product_5)
        VOID.Glide(false, mContext, post.postimage6, holder.image_product_6)
        VOID.Glide(false, mContext, post.postimage7, holder.image_product_7)
        VOID.Glide(false, mContext, post.postimage8, holder.image_product_8)
        VOID.Glide(false, mContext, post.postimage9, holder.image_product_9)
        VOID.Glide(false, mContext, post.postimage10, holder.image_product_10)

        if (post.postimage2 == DATA.EMPTY) {
            holder.image_product_2.visibility = View.GONE
        } else {
            holder.image_product_2.visibility = View.VISIBLE
        }
        if (post.postimage3 == DATA.EMPTY) {
            holder.image_product_3.visibility = View.GONE
        } else {
            holder.image_product_3.visibility = View.VISIBLE
        }
        if (post.postimage4 == DATA.EMPTY) {
            holder.image_product_4.visibility = View.GONE
        } else {
            holder.image_product_4.visibility = View.VISIBLE
        }
        if (post.postimage5 == DATA.EMPTY) {
            holder.image_product_5.visibility = View.GONE
        } else {
            holder.image_product_5.visibility = View.VISIBLE
        }
        if (post.postimage6 == DATA.EMPTY) {
            holder.image_product_6.visibility = View.GONE
        } else {
            holder.image_product_6.visibility = View.VISIBLE
        }
        if (post.postimage7 == DATA.EMPTY) {
            holder.image_product_7.visibility = View.GONE
        } else {
            holder.image_product_7.visibility = View.VISIBLE
        }
        if (post.postimage8 == DATA.EMPTY) {
            holder.image_product_8.visibility = View.GONE
        } else {
            holder.image_product_8.visibility = View.VISIBLE
        }
        if (post.postimage9 == DATA.EMPTY) {
            holder.image_product_9.visibility = View.GONE
        } else {
            holder.image_product_9.visibility = View.VISIBLE
        }
        if (post.postimage10 == DATA.EMPTY) {
            holder.image_product_10.visibility = View.GONE
        } else {
            holder.image_product_10.visibility = View.VISIBLE
        }
        if (post.postimage2 == DATA.EMPTY && post.postimage3 == DATA.EMPTY && post.postimage4 == DATA.EMPTY && post.postimage5 == DATA.EMPTY && post.postimage6 == DATA.EMPTY && post.postimage7 == DATA.EMPTY && post.postimage8 == DATA.EMPTY && post.postimage9 == DATA.EMPTY && post.postimage10 == DATA.EMPTY) {
            holder.scroll_image.visibility = View.GONE
        } else {
            holder.scroll_image.visibility = View.VISIBLE
        }
        if (post.name == DATA.EMPTY) {
            holder.product_name.visibility = View.GONE
        } else {
            holder.product_name.visibility = View.VISIBLE
            holder.product_name.text = post.name
        }
        if (post.price == DATA.EMPTY) {
            holder.price_product.visibility = View.GONE
        } else {
            holder.price_product.visibility = View.VISIBLE
            holder.price_product.text = MessageFormat.format("{0} SYP", post.price)
        }
        if (post.indications == DATA.EMPTY) {
            holder.linear_indications.visibility = View.GONE
            holder.linear_indications2.visibility = View.GONE
        } else {
            holder.linear_indications.visibility = View.VISIBLE
            holder.text_indications.visibility = View.VISIBLE
            holder.linear_indications2.visibility = View.VISIBLE
            holder.indications.visibility = View.VISIBLE
            holder.indications.text = post.indications
        }
        if (post.use == DATA.EMPTY) {
            holder.linear_how_to_use.visibility = View.GONE
            holder.linear_how_to_use2.visibility = View.GONE
        } else {
            holder.linear_how_to_use.visibility = View.VISIBLE
            holder.text_how_to_use.visibility = View.VISIBLE
            holder.linear_how_to_use2.visibility = View.VISIBLE
            holder.how_to_use.visibility = View.VISIBLE
            holder.how_to_use.text = post.use
        }

        isLiked(post.postid, holder.like)
        isSaved(post.postid, holder.save)
        nrLikes(holder.like_number, post.postid)

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
        holder.like.setOnClickListener {
            if (holder.like.tag == "like") {
                FirebaseDatabase.getInstance().reference.child(DATA.LIKES).child(post.postid!!)
                    .child(DATA.FirebaseUserUid).setValue(true)
            } else {
                FirebaseDatabase.getInstance().reference.child(DATA.LIKES).child(post.postid!!)
                    .child(DATA.FirebaseUserUid).removeValue()
            }
        }
        holder.image_product_1.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage, holder.image_product)
        }
        holder.image_product_2.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage2, holder.image_product)
        }
        holder.image_product_3.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage3, holder.image_product)
        }
        holder.image_product_4.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage4, holder.image_product)
        }
        holder.image_product_5.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage5, holder.image_product)
        }
        holder.image_product_6.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage6, holder.image_product)
        }
        holder.image_product_7.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage7, holder.image_product)
        }
        holder.image_product_8.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage8, holder.image_product)
        }
        holder.image_product_9.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage9, holder.image_product)
        }
        holder.image_product_10.setOnClickListener {
            VOID.Glide(false, mContext, post.postimage10, holder.image_product)
        }
    }

    override fun getItemCount(): Int {
        return mPost!!.size
    }

    class ViewHolder(val binding: ItemPostDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        val image_product: ImageView = binding.imageProduct
        val save: ImageView = binding.save
        val like: ImageView = binding.like
        val image_product_1: ImageView = binding.imageProduct1
        val image_product_2: ImageView = binding.imageProduct2
        val image_product_3: ImageView = binding.imageProduct3
        val image_product_4: ImageView = binding.imageProduct4
        val image_product_5: ImageView = binding.imageProduct5
        val image_product_6: ImageView = binding.imageProduct6
        val image_product_7: ImageView = binding.imageProduct7
        val image_product_8: ImageView = binding.imageProduct8
        val image_product_9: ImageView = binding.imageProduct9
        val image_product_10: ImageView = binding.imageProduct10
        val product_name: TextView = binding.productName
        val price_product: TextView = binding.priceProduct
        val like_number: TextView = binding.likeNumber
        val text_indications: TextView = binding.textIndications
        val indications: TextView = binding.indications
        val text_how_to_use: TextView = binding.textHowToUse
        val how_to_use: TextView = binding.howToUse
        val linear_indications: LinearLayout = binding.linearIndications
        val linear_indications2: LinearLayout = binding.linearIndications2
        val linear_how_to_use: LinearLayout = binding.linearHowToUse
        val linear_how_to_use2: LinearLayout = binding.linearHowToUse2
        val scroll_image: HorizontalScrollView = binding.scrollImage
    }

    private fun isLiked(postId: String?, imageView: ImageView) {
        val reference = FirebaseDatabase.getInstance().reference
            .child(DATA.LIKES).child(postId!!)
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
    }
}