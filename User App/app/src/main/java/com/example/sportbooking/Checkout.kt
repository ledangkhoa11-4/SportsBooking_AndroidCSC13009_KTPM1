package com.example.sportbooking

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Checkout : AppCompatActivity() {
    private lateinit var creditCardNumberEditText: EditText
    private lateinit var fullNameEditText: EditText
    private lateinit var checkoutButton: Button
    private lateinit var checkoutLaterButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        creditCardNumberEditText = findViewById(R.id.creditCardNumberEditText)
        fullNameEditText = findViewById(R.id.fullNameEditText)
        checkoutButton = findViewById(R.id.checkoutButton)
        checkoutLaterButton = findViewById(R.id.checkoutLaterButton)

        checkoutButton.setOnClickListener {
            val creditCardNumber = creditCardNumberEditText.text.toString()
            val fullName = fullNameEditText.text.toString()
            if (creditCardNumber.isEmpty() || fullName.isEmpty()) {
                return@setOnClickListener
            }
        }

        checkoutLaterButton.setOnClickListener {
            finish()
        }
    }
}