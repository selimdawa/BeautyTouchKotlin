package com.flatcode.beautytouch.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import coil3.load
import com.flatcode.beautytouch.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smarteist.autoimageslider.SliderViewAdapter
import java.util.*

class ImageSliderAdapter(var context: Context?, var setTotalCount: Int) :
    SliderViewAdapter<ImageSliderAdapter.SliderViewHolder>() {

    var ImageLink: String? = null

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        FirebaseDatabase.getInstance().getReference("ImageLinks")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    when (position) {
                        0 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("1").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        1 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("2").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        2 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("3").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        3 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("4").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        4 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("5").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        5 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("6").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        6 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("7").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        7 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("8").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        8 -> {
                            ImageLink = Objects.requireNonNull(snapshot.child("9").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        9 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("10").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        10 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("11").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        11 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("12").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        12 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("13").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        13 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("14").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        14 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("15").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        15 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("16").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        16 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("17").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        17 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("18").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        18 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("19").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                        19 -> {
                            ImageLink =
                                Objects.requireNonNull(snapshot.child("20").value).toString()
                            viewHolder.Imageslider.load(ImageLink)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    override fun getCount(): Int {
        return setTotalCount
    }

    class SliderViewHolder(var itemView: View) : ViewHolder(
        itemView
    ) {
        var Imageslider: ImageView

        init {
            Imageslider = itemView.findViewById(R.id.imageView)
        }
    }
}