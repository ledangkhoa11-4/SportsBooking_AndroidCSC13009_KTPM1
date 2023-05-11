package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sportbooking.Adapters.ListMangeUserAdapter
import com.example.sportbooking.DTO.Message
import com.example.sportbooking.DTO.Owner
import com.example.sportbooking.DTO.User
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ListMessage : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: MaterialToolbar
    lateinit var ownerMessageList:ArrayList<Owner>
    var ownerList= SignInActivity.ownerList
    companion object{
        var lastMessList=HashMap<String,Message>()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_message)
        toolbar=findViewById(R.id.materialToolbar)
        recyclerView=findViewById(R.id.ListUserRCV)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        ownerMessageList= ArrayList()
        recyclerView.layoutManager= LinearLayoutManager(this)
        val adapter= ListMangeUserAdapter(this,ownerMessageList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager= LinearLayoutManager(this)

        MainActivity.database.getReference("chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    ownerMessageList.clear()
                    for(ds in snapshot.children){
                        val keyarray= ds.key?.split(",")
                        Log.i("keyarray",keyarray.toString())
                        if(keyarray?.get(0)=="user"+MainActivity.user.id){
                            for(i in 0  until ownerList.size){
                                if(keyarray[1]=="owner"+ownerList[i].id){
                                    ownerMessageList.add(ownerList[i])

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
        MainActivity.database.getReference("chats").addValueEventListener(object :
            ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val key_item=ds.key!!.split(",")

                    if(key_item[0]=="owner" + MainActivity.user.id ){
                        var lastMess: Message?=null
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