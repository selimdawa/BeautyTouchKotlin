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
        binding = ItemPostDetailBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding!!.root)
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

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var image_product: ImageView
        var save: ImageView
        var like: ImageView
        var image_product_1: ImageView
        var image_product_2: ImageView
        var image_product_3: ImageView
        var image_product_4: ImageView
        var image_product_5: ImageView
        var image_product_6: ImageView
        var image_product_7: ImageView
        var image_product_8: ImageView
        var image_product_9: ImageView
        var image_product_10: ImageView
        var product_name: TextView
        var price_product: TextView
        var like_number: TextView
        var text_indications: TextView
        var indications: TextView
        var text_how_to_use: TextView
        var how_to_use: TextView
        var linear_indications: LinearLayout
        var linear_indications2: LinearLayout
        var linear_how_to_use: LinearLayout
        var linear_how_to_use2: LinearLayout
        var scroll_image: HorizontalScrollView

        init {
            image_product = binding!!.imageProduct
            product_name = binding!!.productName
            save = binding!!.save
            price_product = binding!!.priceProduct
            like_number = binding!!.likeNumber
            like = binding!!.like
            linear_indications = binding!!.linearIndications
            text_indications = binding!!.textIndications
            linear_indications2 = binding!!.linearIndications2
            indications = binding!!.indications
            linear_how_to_use = binding!!.linearHowToUse
            text_how_to_use = binding!!.textHowToUse
            linear_how_to_use2 = binding!!.linearHowToUse2
            how_to_use = binding!!.howToUse
            image_product_1 = binding!!.imageProduct1
            image_product_2 = binding!!.imageProduct2
            image_product_3 = binding!!.imageProduct3
            image_product_4 = binding!!.imageProduct4
            image_product_5 = binding!!.imageProduct5
            image_product_6 = binding!!.imageProduct6
            image_product_7 = binding!!.imageProduct7
            image_product_8 = binding!!.imageProduct8
            image_product_9 = binding!!.imageProduct9
            image_product_10 = binding!!.imageProduct10
            scroll_image = binding!!.scrollImage
        }
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
        private var binding: ItemPostDetailBinding? = null
    }
}