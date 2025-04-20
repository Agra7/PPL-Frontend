package com.example.becycle

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.becycle.R

class MainActivity : AppCompatActivity() {
    private lateinit var loginForm: LinearLayout
    private lateinit var signupForm: LinearLayout
    private lateinit var loginTabButton: Button
    private lateinit var signupTabButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginForm = findViewById(R.id.login_form)
        signupForm = findViewById(R.id.signup_form)

        loginTabButton = findViewById(R.id.login_tab_button)
        signupTabButton = findViewById(R.id.signup_tab_button)

        loginTabButton.setOnClickListener {
            showLoginForm()
        }

        signupTabButton.setOnClickListener {
            showSignupForm()
        }

        showLoginForm() // Default to login
    }

    private fun showLoginForm() {
        loginForm.visibility = View.VISIBLE
        signupForm.visibility = View.GONE
        loginTabButton.isEnabled = false
        signupTabButton.isEnabled = true
    }

    private fun showSignupForm() {
        loginForm.visibility = View.GONE
        signupForm.visibility = View.VISIBLE
        loginTabButton.isEnabled = true
        signupTabButton.isEnabled = false
    }
}
