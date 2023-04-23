package com.example .sportbooking_owner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbooking_owner.DTO.User
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListMessageUserActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: MaterialToolbar
    lateinit var userMessageList:ArrayList<User>
    var userList=SignIn.userList
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
        val adapter=ListMangeUserAdapter(this,userMessageList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this)
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
}