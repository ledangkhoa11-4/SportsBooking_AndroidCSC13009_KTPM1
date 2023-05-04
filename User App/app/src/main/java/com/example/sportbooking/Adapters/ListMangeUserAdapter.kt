package com.example.sportbooking.Adapters


import android.content.Context
import android.content.Intent
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbooking.ChatWithOwnerActivity
import com.example.sportbooking.DTO.Owner
import com.example.sportbooking.DTO.User
import com.example.sportbooking.R
import de.hdodenhof.circleimageview.CircleImageView


class ListMangeUserAdapter(val context: Context,val ownerList:ArrayList<Owner>):RecyclerView.Adapter<ListMangeUserAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView

        val avatar:CircleImageView

        init {
            // Define click listener for the ViewHolder's View.
            userName = view.findViewById(R.id.UserNameMessageV)
            avatar=view.findViewById(R.id.AvatarCIMV)



        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.list_message_user_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ownerList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.userName.setText(ownerList[position].username)
            if(ownerList[position].Image!=null){
                holder.avatar.setImageBitmap(ownerList[position].Image)
            }

            holder.itemView.setOnClickListener {
                val intent=Intent(context, ChatWithOwnerActivity::class.java)
                intent.putExtra("Name",ownerList[position].username)
                intent.putExtra("IDReceive",ownerList[position].id)
                Log.i("owner",ownerList[position].id)
                context.startActivity(intent)

            }
    }
}