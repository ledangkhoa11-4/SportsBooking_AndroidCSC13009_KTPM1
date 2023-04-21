package com.example.sportbooking.Adapters

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.sportbooking.DTO.RatingCourt
import com.example.sportbooking.R
import com.ms.square.android.expandabletextview.ExpandableTextView
import java.text.SimpleDateFormat
import java.util.*

class RatingRecyclerViewAdapter(
    private val context: Activity, private val maintitles: List<RatingCourt>) : ArrayAdapter<RatingCourt>(context,
    R.layout.reviews_layout, maintitles) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView: View = inflater.inflate(R.layout.reviews_layout, null, true)
        var image:ImageView = rowView.findViewById(R.id.avatarTv)
        var userName:TextView = rowView.findViewById(R.id.username)
        var ratingBar:RatingBar = rowView.findViewById(R.id.ratingBar)
        var ratingDate:TextView = rowView.findViewById(R.id.ratingDate)
        val commentContainer:ExpandableTextView = rowView.findViewById(R.id.commentContainer)

        var improvement:TextView = rowView.findViewById(R.id.improvements)

        if(maintitles[position].userImage != null){
            image.setImageBitmap(maintitles[position].userImage)
        }
        ratingDate.text = convertDate(maintitles[position].dateRate)
        userName.text = maintitles[position].userName
        ratingBar.numStars = 5
        ratingBar.rating = maintitles[position].star!!
        commentContainer.text = maintitles[position].content
        var improveContent = ""
        if(maintitles[position].improvement!![0]) improveContent += "Chất lượng phục vụ - "
        if(maintitles[position].improvement!![1]) improveContent += "Hỗ trợ khách hàng - "
        if(maintitles[position].improvement!![2]) improveContent += "Thông tin sân - "
        if(maintitles[position].improvement!![3]) improveContent += "Thái độ nhân viên - "
        if(maintitles[position].improvement!![4]) improveContent += "Bề mặt sân - "
        if(maintitles[position].improvement!![5]) improveContent += "Banh - "
        if(improveContent != "")
            improveContent = improveContent.substring(0, improveContent.length - 3)
        improvement.text = improveContent
        return rowView
    }
    fun convertDate(timeStamp:Long):String{
        val sdf = SimpleDateFormat("MMMM dd, yyyy") // create a SimpleDateFormat object with desired format
        val formattedTime = sdf.format(Date(timeStamp)) // convert timestamp to date and format as string
        return formattedTime
    }
}