package com.example .sportbooking_owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbooking_owner.DTO.Message
import com.example.sportbooking_owner.DTO.User
import com.example.sportbooking_owner.ListMangeUserAdapter
import com.example.sportbooking_owner.MainActivity
import com.example.sportbooking_owner.R
import com.example.sportbooking_owner.SignIn
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListMessageUserActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: MaterialToolbar
    lateinit var userMessageList:ArrayList<User>
    var userList= SignIn.userList
    companion object{
    var lastMessList=HashMap<String,Message>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message_user)
        toolbar=findViewById(R.id.materialToolbar)
        recyclerView=findViewById(R.id.ListUserRCV)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        userMessageList= ArrayList()
        recyclerView.layoutManager=LinearLayoutManager(this)
        val adapter= ListMangeUserAdapter(this,userMessageList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this)
        getLastMessage()
        MainActivity.database.getReference("chats")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    userMessageList.clear()
                    for(ds in snapshot.children){
                        val keyarray= ds.key?.split(",")
                        Log.i("keyarray",keyarray.toString())
                        if(keyarray?.get(1)=="owner"+SignIn.owner.id){
                            for(i in 0  until userList.size){
                                if(keyarray[0]=="user"+userList[i].id){
                                    userMessageList.add(userList[i])

                                }
                            }

                        }
                    }
                    adapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

    }
    fun getLastMessage(){
        MainActivity.database.getReference("chats").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val key_item=ds.key!!.split(",")

                    if(key_item[0]=="owner" + SignIn.owner.id ){
                        var lastMess:Message?=null
                        for(message in ds.children){
                            val mess=message.getValue(Message::class.java)
                            if (mess != null) {
                                lastMess=mess
                            }
                        }
                        lastMessList[key_item[1]]=lastMess!!
                    }
                }
            }
        })
    }
}