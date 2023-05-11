package com.example.sportbooking_owner

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbooking_owner.Adapter.MessageAdapter
import com.example.sportbooking_owner.DTO.Message
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ChatWithUserActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendBtn: ImageView
    private lateinit var toolbar: MaterialToolbar
    private lateinit var messageList:ArrayList<Message>
    private lateinit var messageAdapter: MessageAdapter
    val chatRef= MainActivity.database.reference.child("chats")

    var senderRoom:String?=null
    var receiveRoom:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_with_user)
        messageRecyclerView=findViewById(R.id.messagesRCV)
        messageBox=findViewById(R.id.messageBoxEdt)
        sendBtn=findViewById(R.id.SendBtnImV)
        toolbar=findViewById(R.id.materialToolbar)
        val name=intent.getStringExtra("Name")
        val userID=intent.getStringExtra("ID")
        toolbar.title = name
        toolbar.setNavigationOnClickListener {
            finish()
        }
        senderRoom="owner"+SignIn.owner?.id+","+"user"+userID
        Log.i("message",userID.toString())
        receiveRoom="user"+userID+","+"owner"+SignIn.owner?.id
        messageList= ArrayList()
        messageAdapter= MessageAdapter(this,messageList!!)
        messageRecyclerView.adapter=messageAdapter
        messageRecyclerView.layoutManager  = LinearLayoutManager(this)
        chatRef.child(senderRoom!!).child("messages").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                messageList.clear()
                for(ds in snapshot.children){
                    val message=ds.getValue(Message::class.java)

                    messageList.add(message!!)
                }
                Log.i("message",messageList.size.toString())
                messageAdapter.notifyDataSetChanged()
            }
        })

        sendBtn.setOnClickListener {
            val mess=messageBox.text.toString()
            val messObject= Message(mess,"owner"+SignIn.owner?.id.toString())
            chatRef.child(senderRoom!!)
                .child("messages").push().setValue(messObject)
                .addOnSuccessListener {
                    chatRef.child(receiveRoom!!)
                        .child("messages").push().setValue(messObject)
                }
            messageBox.setText("")
        }


    }
}