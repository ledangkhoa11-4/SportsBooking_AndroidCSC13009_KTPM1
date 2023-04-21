package com.example.sportbooking.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.sportbooking.DTO.Province
import com.example.sportbooking.R

class ProvinceSpinnerAdapter (private val context: Context, private val items: Array<Province>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(p0: Int): Any {
        return items[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0L
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val view = p1?:LayoutInflater.from(context).inflate(R.layout.spinner_item, p2,false)
        val tv = view.findViewById<TextView>(R.id.name)
        tv.setText(items[p0].name)
        return view
    }
}