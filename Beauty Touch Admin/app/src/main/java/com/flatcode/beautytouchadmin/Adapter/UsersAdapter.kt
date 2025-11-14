package com.flatcode.beautytouchadmin.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.flatcode.beautytouchadmin.Model.User
import com.flatcode.beautytouchadmin.Unit.CLASS
import com.flatcode.beautytouchadmin.Unit.DATA
import com.flatcode.beautytouchadmin.Unit.VOID
import com.flatcode.beautytouchadmin.databinding.ItemUserBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.MessageFormat

class UsersAdapter(private val mContext: Context, private val mUser: List<User?>) :
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemUserBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return ViewHolder(binding!!.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = mUser[position]
        val id = DATA.EMPTY + user!!.id

        VOID.Glide(true, mContext, user.imageurl, holder.image)
        if (user.username == DATA.EMPTY) {
            holder.name.visibility = View.GONE
        } else {
            holder.name.visibility = View.VISIBLE
            holder.name.text = user.username
        }

        nrFavorites(holder.favorites, id)
        holder.card.setOnClickListener {
            VOID.IntentExtra(mContext, CLASS.USER_DETAILS, DATA.PROFILE_ID, id)
        }
    }

    override fun getItemCount(): Int {
        return mUser.size
    }

    class ViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
        var image: ImageView
        var name: TextView
        var favorites: TextView
        var card: CardView

        init {
            image = binding!!.image
            name = binding!!.name
            favorites = binding!!.favorites
            card = binding!!.card
        }
    }

    private fun nrFavorites(favorites: TextView, userid: String) {
        val reference = FirebaseDatabase.getInstance().getReference(DATA.SAVES).child(userid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                favorites.text = MessageFormat.format("{0}", dataSnapshot.childrenCount)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    companion object {
        private var binding: ItemUserBinding? = null
    }
}
