package com.example.sportbooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UserTabActivity : AppCompatActivity() {
    lateinit var nav_bar: NavigationBarView
    lateinit var editBtn: Button
    lateinit var usernameTv:TextView
    lateinit var useremailTv:TextView
    lateinit var avatarIv:ImageView
    lateinit var messageListBtn:Button
    lateinit var logoutBtn:Button
    val currentUser = MainActivity.user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_tab)
        nav_bar = findViewById(R.id.nav_bar)
        navBarHandle(nav_bar)
        editBtn = findViewById(R.id.editBtn)
        editBtn.setOnClickListener {
            startActivityForResult(Intent(this,EditInformationActivity::class.java),200)
        }

        usernameTv = findViewById(R.id.user_nameTv)
        useremailTv = findViewById(R.id.user_emailTv)
        avatarIv = findViewById(R.id.avatarTv)
        usernameTv.setText(currentUser.username)
        useremailTv.setText(currentUser.email)
        if(currentUser.Image!=null){
            avatarIv.setImageBitmap(currentUser.Image)
        }
        findViewById<Button>(R.id.favoriteCourtBtn).setOnClickListener {
            val intent = Intent(this, MyFavoriteActivity::class.java)
            startActivity(intent)
        }
        messageListBtn=findViewById(R.id.messageListBtn)
        messageListBtn.setOnClickListener {
            startActivity(Intent(this,ListMessage::class.java))
        }
        logoutBtn = findViewById(R.id.logoutBtn)
        logoutBtn.setOnClickListener {
            val auth = Firebase.auth
            auth.signOut()
            finish()
            startActivity(Intent(this, SignInActivity::class.java))
        }

    }
    fun navBarHandle(nav_bar: NavigationBarView){
        nav_bar.selectedItemId = R.id.item_user
        nav_bar.setOnItemSelectedListener {
            when(it.itemId){
                R.id.item_home->{
                    startActivity(Intent(this,HomeActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                R.id.item_user->{
                    true
                }
                R.id.item_schedule->{
                    startActivity(Intent(this,MyBookingActivity::class.java))
                    overridePendingTransition(0,0)
                    finish()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        usernameTv = findViewById(R.id.user_nameTv)
        useremailTv = findViewById(R.id.user_emailTv)
        avatarIv = findViewById(R.id.avatarTv)
        usernameTv.setText(currentUser.username)
        useremailTv.setText(currentUser.email)
        if(currentUser.Image!=null){
            avatarIv.setImageBitmap(currentUser.Image)
        }
    }
}