package com.example.sportbooking_owner


import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbooking_owner.DTO.User
import de.hdodenhof.circleimageview.CircleImageView


class ListMangeUserAdapter(val context: Context,val userList:ArrayList<User>):RecyclerView.Adapter<ListMangeUserAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName: TextView
        val lastMess: TextView
        val avatar:CircleImageView

        init {
            // Define click listener for the ViewHolder's View.
            userName = view.findViewById(R.id.UserNameMessageV)
            avatar=view.findViewById(R.id.AvatarCIMV)
            lastMess=view.findViewById(R.id.LastMessTV)


        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(context).inflate(R.layout.list_message_user_item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.userName.setText(userList[position].username)
            if(userList[position].Image!=null){
                holder.avatar.setImageBitmap(userList[position].Image)
            }
            holder.lastMess.setText(ListMessageUserActivity.lastMessList[userList[position].id].toString())
            holder.itemView.setOnClickListener {
                val intent=Intent(context,ChatWithUserActivity::class.java)
                intent.putExtra("Name",userList[position].username)
                intent.putExtra("ID",userList[position].id)
                context.startActivity(intent)

            }
    }
}