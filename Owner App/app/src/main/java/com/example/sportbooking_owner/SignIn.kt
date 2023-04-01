package com.example.sportbooking_owner

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


class SignIn : AppCompatActivity() {
    var signInBtn:Button?=null
    var signInGGBtn:Button?=null
    var signInFbBtn:Button?=null
    var signUp:Button?=null
    var mGoogleSignInClient:GoogleSignInClient?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signInBtn=findViewById<Button>(R.id.SignInBtn)
        signInGGBtn=findViewById<Button>(R.id.SignInGGBtn)
        signInFbBtn=findViewById<Button>(R.id.SignInFbBtn)
        signUp=findViewById<Button>(R.id.SignUpBtn)

        //Sign Up
        signUp!!.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
        }
        //Sign In

        //Sign In with google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
        val account=GoogleSignIn.getLastSignedInAccount(this)
        if(account!=null){
            //sign in success
            startActivity(Intent(this,SignUp::class.java))
        }
        signInGGBtn!!.setOnClickListener {
            signIn()
        }
    }
    private fun signIn(){
        val signInIntent=mGoogleSignInClient!!.signInIntent
        startActivityForResult(signInIntent,1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1000){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            startActivity(Intent(this,CourtListActivity::class.java))
            // Signed in successfully, show authenticated UI.
            //updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            //updateUI(null)
        }
    }
}