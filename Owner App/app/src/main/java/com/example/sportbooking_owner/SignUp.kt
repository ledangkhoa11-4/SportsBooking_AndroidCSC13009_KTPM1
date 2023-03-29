package com.example.sportbooking_owner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class SignUp : AppCompatActivity() {
    var mGoogleSignInClient: GoogleSignInClient?=null
    var gso:GoogleSignInOptions?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var signUpBtn=findViewById<Button>(R.id.SignUpBtn)
        var name=findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.LayoutName).editText
        signUpBtn.setOnClickListener {
            signOut()
        }
        gso= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient= GoogleSignIn.getClient(this, gso!!)
        var acct=GoogleSignIn.getLastSignedInAccount(this)
        if(acct!=null){
            name?.setText(acct.displayName)
        }
    }

    private fun signOut() {
        mGoogleSignInClient!!.signOut().addOnCompleteListener {
            finish()
            startActivity(Intent(this,SignIn::class.java))
        }
    }
}