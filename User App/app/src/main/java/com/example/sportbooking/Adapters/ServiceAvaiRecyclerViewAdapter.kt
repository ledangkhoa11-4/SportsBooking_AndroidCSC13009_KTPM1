package com.example.sportbooking.Adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbooking.R

class ServiceAvaiRecyclerViewAdapter(var packageName:String, var resources: Resources, var listService: ArrayList<String>) :
    RecyclerView.Adapter<ServiceAvaiRecyclerViewAdapter.ViewHolder>() {
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        val serviceImage = listItemView.findViewById<ImageView>(R.id.serviceImage)
        val serviceName = listItemView.findViewById<TextView>(R.id.serviceName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val contactView = inflater.inflate(R.layout.service_available_layout, parent, false)
        return ViewHolder(contactView)
    }
    override fun getItemCount(): Int {
        return listService.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service: String = listService.get(position)
        var iconName = service.lowercase()
        iconName = iconName.replace(" ","_")
        iconName += "_icon"
        val drawableId = resources.getIdentifier(iconName,"drawable",packageName)
        holder.serviceImage.setImageDrawable(resources.getDrawable(drawableId))
        holder.serviceName.setText(service)
    }
}