package com.example.sportbooking_owner

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.ktx.Firebase

class SignUp : AppCompatActivity() {
    var mGoogleSignInClient: GoogleSignInClient?=null
    var gso:GoogleSignInOptions?=null
    lateinit var nameEdt:EditText
    lateinit var emailEdt:EditText
    lateinit var passwordEdt:EditText
    lateinit var repasswordEdt:EditText
    lateinit var auth:FirebaseAuth
    lateinit var database:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        var signUpBtn=findViewById<Button>(R.id.SignUpBtn)
        nameEdt= findViewById<TextInputLayout>(R.id.LayoutName).editText!!
        emailEdt= findViewById<TextInputLayout>(R.id.LayoutEmail).editText!!
        passwordEdt=findViewById<TextInputLayout>(R.id.LayoutPassword).editText!!
        repasswordEdt=findViewById<TextInputLayout>(R.id.LayoutRepassword).editText!!
        repasswordEdt.doOnTextChanged { text, start, before, count ->
            val repasswordLayout=findViewById<TextInputLayout>(R.id.LayoutRepassword)
            if(passwordEdt.text.toString() == text.toString()){

                repasswordLayout.error=null

            }
            else{
                repasswordLayout.error="Confirm password is not corrected"

            }
        }

        auth= Firebase.auth
        database=MainActivity.database.reference
        signUpBtn.setOnClickListener {
            val email=emailEdt.text.toString()
            val password=passwordEdt.text.toString()
            val username=nameEdt.text.toString()
            val repassword=repasswordEdt.text.toString()

            if (email != "" && password != "" && username!="" && password!="" && repassword!="") {

                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("stream", "createUserWithEmail:success")
                                val user = auth.currentUser
                                if (user != null) {
                                    var currentUser=User_Owner(user.uid,username,email)
                                    writeNewUser(currentUser)
                                }
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w("stream", "createUserWithEmail:failure", task.exception)

                                Toast.makeText(
                                    baseContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                showErrorDialog(task.exception?.message.toString())

                            }
                    }
            }
            else{
                showErrorDialog("Fields can't be empty")
            }
        }

    }
    fun showErrorDialog(error:String){
        val builder = AlertDialog.Builder(this)
        builder.setIcon(R.drawable.baseline_error_outline_24).setTitle("Error")
            .setMessage("$error\n Please sign up again!!")

            .setPositiveButton("Done", DialogInterface.OnClickListener { dialog, id ->

            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, id ->

            })


        var dialog:AlertDialog?=null
        dialog=builder.create()
        dialog.show()
    }
    fun writeNewUser(user:User_Owner) {
        database.child("Owner").push().setValue(user)
    }


}