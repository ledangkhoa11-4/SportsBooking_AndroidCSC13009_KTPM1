package com.example.sportbooking_owner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class VerifyEmailActivity : AppCompatActivity() {
    lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_email)
        auth=Firebase.auth
        val curUser=auth.currentUser
        if (curUser != null) {

            if(curUser.isEmailVerified){
                startActivity(Intent(this,CourtListActivity::class.java))
            }
            else{
                curUser.sendEmailVerification()
            }
        }

    }

    override fun onRestart() {
        super.onRestart()

    }
    override fun onResume() {
        super.onResume()
        auth=Firebase.auth
        val curUser=auth.currentUser
        if (curUser != null) {
            if(curUser.isEmailVerified){
                startActivity(Intent(this,CourtListActivity::class.java))
            }
            else{
                curUser.sendEmailVerification()
            }
        }
    }
}