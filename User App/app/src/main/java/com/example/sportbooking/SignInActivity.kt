package com.example.sportbooking

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignInActivity : AppCompatActivity() {
    var signInBtn: Button?=null
    var signInGGBtn: Button?=null
    var signInFbBtn: Button?=null
    var signUp: Button?=null

    private lateinit var auth: FirebaseAuth
    lateinit var emailEdt: TextInputLayout
    lateinit var passwordEdt: TextInputLayout
    var currentUser:User?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val database = Firebase.database
        signInBtn=findViewById<Button>(R.id.SignInBtn)
        signInGGBtn=findViewById<Button>(R.id.SignInGGBtn)
        signInFbBtn=findViewById<Button>(R.id.SignInFbBtn)
        signUp=findViewById<Button>(R.id.SignUpBtn)
        emailEdt=findViewById(R.id.LayoutEmail)
        passwordEdt=findViewById(R.id.LayoutPassword)
        auth = Firebase.auth

        //Sign Up
        signUp!!.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }

        //Sign In

        signInBtn!!.setOnClickListener {
            val email= emailEdt.editText?.text.toString()
            val password=passwordEdt.editText?.text.toString()
            if (email!="" && password!="" ){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            val user = auth.currentUser

                            startActivity(Intent(this, HomeActivity::class.java))
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
            }

        }
        //Sign In with google
        signInGGBtn!!.setOnClickListener {

        }


    }

    private fun updateUI(user: User?) {
        startActivity(Intent(this, HomeActivity::class.java))
    }


    public override fun onStart() {
        super.onStart()
        auth.signOut()
        // Check if user is signed in (non-null) and update UI accordingly.
//        val user = auth.currentUser
//        currentUser=User(user?.uid,user?.displayName.toString(),user?.email)
//        if(currentUser != null){
//           startActivity(Intent(this, CourtListActivity::class.java))
//        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }


}