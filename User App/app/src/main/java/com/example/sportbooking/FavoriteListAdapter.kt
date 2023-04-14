package com.example.sportbooking

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class   FavoriteListAdapter(private val context: Activity, private val courtInfo:ArrayList<Court>): ArrayAdapter<Court>(context,R.layout.favorite_item,courtInfo) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.favorite_item, null, true)
        val courtName = rowView.findViewById(R.id.courtNameFavorite) as TextView
        val courtImage = rowView.findViewById(R.id.courtImageFavorite) as ImageView

        courtName.text = courtInfo[position].Name
        if(courtInfo[position].bitmapArrayList.size > 0)
            courtImage.setImageBitmap(courtInfo[position].bitmapArrayList[0])

        return rowView
    }
}