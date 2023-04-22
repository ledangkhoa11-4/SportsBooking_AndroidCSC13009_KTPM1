package com.example.sportbo

import android.view.LayoutInflater



import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.sportbooking.DTO.Message
import com.example.sportbooking.MainActivity
import com.example.sportbooking.R


class MessageAdapter(val context:Context, val messageList: ArrayList<Message>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_SEND=2
    val ITEM_RECEIVE=1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if(viewType==ITEM_SEND){
            val view:View=LayoutInflater.from(context).inflate(R.layout.send_message,parent,false)
            return SentViewHolder(view)
        }
        else{
            val view:View=LayoutInflater.from(context).inflate(R.layout.receive_message,parent,false)
            return ReceiveViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage=messageList[position]
        if("user"+  MainActivity.user.id == currentMessage.senderId){
            return ITEM_SEND
        }
        else{
            return ITEM_RECEIVE
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentMessage=messageList[position]
        if(holder.javaClass==SentViewHolder::class.java){
            val viewHolder=holder as SentViewHolder
            holder.sentTv.text=currentMessage.message
        }else{
            val viewHolder=holder as ReceiveViewHolder
            holder.receiveTV.text=currentMessage.message
        }
    }
    class SentViewHolder(item: View):RecyclerView.ViewHolder(item){
            val sentTv=itemView.findViewById<TextView>(R.id.sentMessageTv)
    }
    class ReceiveViewHolder(item: View):RecyclerView.ViewHolder(item){
        val receiveTV=itemView.findViewById<TextView>(R.id.receiveMessageTv)
    }

}