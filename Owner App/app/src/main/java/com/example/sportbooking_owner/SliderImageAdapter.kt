package com.example.sportbooking_owner


import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class PagerVH(itemView: View) : RecyclerView.ViewHolder(itemView)
class SliderImageAdapter(val listUri:ArrayList<Uri>, val bitmap:Bitmap? = null, val bitmapList:ArrayList<Bitmap>? = null) : RecyclerView.Adapter<PagerVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerVH =
        PagerVH(LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false))

    override fun getItemCount(): Int = listUri.size

    override fun onBindViewHolder(holder: PagerVH, position: Int) = holder.itemView.run {
        if(bitmap == null && bitmapList == null){
            this.findViewById<ImageView>(R.id.imageViewItem).setImageURI(listUri[position])
        }else{
            if(bitmapList != null){
                this.findViewById<ImageView>(R.id.imageViewItem).setImageBitmap(bitmapList!![position])
            }else
                this.findViewById<ImageView>(R.id.imageViewItem).setImageBitmap(bitmap)
        }
    }
}