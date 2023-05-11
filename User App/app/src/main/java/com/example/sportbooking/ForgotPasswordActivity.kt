package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsMessage.SubmitPdu
import android.widget.Button
import android.widget.ImageButton
import com.example.sportbooking.Ultils.CreateToast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var emailInput:TextInputLayout
    lateinit var submit: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        findViewById<ImageButton>(R.id.backButtonForgotPsw).setOnClickListener { finish() }
        emailInput = findViewById(R.id.emailReset)
        submit = findViewById(R.id.sendEmail)
        submit.setOnClickListener {
            val email = emailInput.editText!!.text.toString()
            if(!isEmailValid(email)){
                CreateToast.createToast(this@ForgotPasswordActivity, "Error", "Please input valid email!",false)
                return@setOnClickListener
            }
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        CreateToast.createToast(this@ForgotPasswordActivity, "Email was sent", "Please check your inbox to reset password!",true)
                        finish()
                    }else{
                        CreateToast.createToast(this@ForgotPasswordActivity, "Error", "Email not exists",false)
                    }
                }
        }
    }
    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}