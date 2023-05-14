package com.example.sportbooking_owner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView

class OwnerTabActivity : AppCompatActivity() {
    val owner=SignIn.owner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_tab)
        val EditBtn=findViewById<Button>(R.id.editBtn)
        val avaImV=findViewById<CircleImageView>(R.id.avatarrEdditImV)
        val ScheduleBtn=findViewById<Button>(R.id.scheduleBtn)
        val LogoutBtn=findViewById<Button>(R.id.LogoutBtn)
        val nav_bar=findViewById<NavigationBarView>(R.id.nav_bar_owner)
        val NameTv=findViewById<TextView>(R.id.NameTv)
        val EmailTv=findViewById<TextView>(R.id.emailTv)

        navBarHandle(nav_bar)
        if(owner!=null){
            NameTv.text = owner.username
            EmailTv.text = owner.email
            avaImV.setImageBitmap(owner.Image)
        }


        EditBtn.setOnClickListener {
            startActivity(Intent(this,EditinformationOwnerAcitvity::class.java))
        }
        LogoutBtn.setOnClickListener {
            val auth=Firebase.auth
            auth.signOut()
            finish()
            startActivity(Intent(this,SignIn::class.java))
        }
        ScheduleBtn.setOnClickListener {
            startActivity(Intent(this,CourtScheduleActivity::class.java))
        }
    }
    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_user
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home-> {
                    startActivity(Intent(this,CourtListActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true}
                R.id.item_user->{
                    true
                }
                R.id.message->{
                    startActivity(Intent(this,ListMessageUserActivity::class.java))
                    true
                }
                else -> {
                    false
                }
            }
        }
    }
}