package com.example.sportbooking

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class ImageViewPagerAdapter(private val imagesList:ArrayList<Bitmap>) : RecyclerView.Adapter<PagerVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.image_items, parent, false))

    override fun getItemCount(): Int = imagesList.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        this.findViewById<ImageView>(R.id.image).setImageBitmap(imagesList[position])
    }
}

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)