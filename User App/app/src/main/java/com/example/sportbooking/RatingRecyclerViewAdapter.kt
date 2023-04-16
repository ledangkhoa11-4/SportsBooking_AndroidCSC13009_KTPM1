package com.example.sportbooking

import android.app.Activity
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.sportbooking.DTO.RatingCourt

class RatingRecyclerViewAdapter(
    private val context: Activity, private val maintitles: List<RatingCourt>) : ArrayAdapter<RatingCourt>(context, R.layout.reviews_layout, maintitles) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.reviews_layout, null, true)
        var image:ImageView = rowView.findViewById(R.id.userImage)
        var userName:TextView = rowView.findViewById(R.id.userName)
        var ratingBar:RatingBar = rowView.findViewById(R.id.ratingBar)
        var content:TextView = rowView.findViewById(R.id.contentTextView)
        var improvement:TextView = rowView.findViewById(R.id.improvement)
        if(maintitles[position].userImage != null) image.setImageBitmap(maintitles[position].userImage)
        else image.setImageResource(R.drawable.camera_icon)
        userName.text = maintitles[position].userName
        ratingBar.rating = maintitles[position].star!!
        content.text = maintitles[position].content
        var improveContent = ""
        if(maintitles[position].improvement!![0]) improveContent += "Chất lượng phục vụ"
        if(maintitles[position].improvement!![1]) improveContent += "\t\t\tHỗ trợ khách hàng"
        if(maintitles[position].improvement!![2]) improveContent += "\t\t\tThông tin sân"
        if(maintitles[position].improvement!![3]) improveContent += "\t\t\tThái độ nhân viên"
        if(maintitles[position].improvement!![4]) improveContent += "\t\t\tBề mặt sân"
        if(maintitles[position].improvement!![5]) improveContent += "\t\t\tBanh"
        improvement.text = improveContent
        return rowView
    }
}