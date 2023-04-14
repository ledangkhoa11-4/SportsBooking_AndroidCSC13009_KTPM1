package com.example.sportbooking_owner

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class ImageViewPagerAdapter(private val imagesList:ArrayList<Bitmap>) : RecyclerView.Adapter<PagerVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false))

    override fun getItemCount(): Int = imagesList.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        this.findViewById<ImageView>(R.id.imageViewItem).setImageBitmap(imagesList[position])

    }
}

//class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)