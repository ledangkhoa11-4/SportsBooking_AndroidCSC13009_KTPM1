package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import com.example.sportbooking.Ultils.CreateToast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var currentPswInput:TextInputLayout
    lateinit var newPswInput:TextInputLayout
    lateinit var confirmNewPswInput:TextInputLayout
    lateinit var submit:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        currentPswInput = findViewById(R.id.currentPsw)
        newPswInput = findViewById(R.id.newPsw)
        confirmNewPswInput = findViewById(R.id.confirmNewPsw)
        submit = findViewById(R.id.changePswBtn)

        newPswInput.editText!!.doOnTextChanged { text, start, before, count ->
            if(text.toString().length<8){
                newPswInput.error = "Minimum length is 8"
            }else{
                newPswInput.error = null
            }
        }
        confirmNewPswInput.editText!!.doOnTextChanged { text, start, before, count ->
            if(text.toString() != newPswInput.editText!!.text.toString()){
                confirmNewPswInput.error = "Confirm password not match"
            }else{
                confirmNewPswInput.error = null
            }
        }
        submit.setOnClickListener {
            val email = MainActivity.user.email
            val currentPsw = currentPswInput.editText!!.text.toString()
            val newPsw = newPswInput.editText!!.text.toString()
            val confirmPsw = confirmNewPswInput.editText!!.text.toString()

            if(newPsw != confirmPsw){
                CreateToast.createToast(this@ChangePasswordActivity,"Error","Confirm password not match",false)
                return@setOnClickListener
            }
            if(newPsw.length < 8){
                CreateToast.createToast(this@ChangePasswordActivity,"Error","Password too short",false)
                return@setOnClickListener
            }
            Firebase.auth.signInWithEmailAndPassword(email, currentPsw)
                .addOnCompleteListener(this){task->
                    if(task.isSuccessful){
                        Firebase.auth.currentUser!!.updatePassword(newPsw)
                            .addOnCompleteListener{
                                CreateToast.createToast(this@ChangePasswordActivity,"Successfully","Password changed successfully",true)
                                finish()
                            }
                    }else{
                        CreateToast.createToast(this@ChangePasswordActivity,"Wrong password","Current password is incorrect",false)
                    }
                }
        }
        findViewById<ImageButton>(R.id.backButtonChangePsw).setOnClickListener { finish() }
    }
}