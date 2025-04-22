package com.example.becycle

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : BaseActivity() {

    private lateinit var loginForm: LinearLayout
    private lateinit var signupForm: LinearLayout
    private lateinit var loginTab: TextView
    private lateinit var signupTab: TextView
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        setContentView(R.layout.activity_main)

        loginForm = findViewById(R.id.login_form)
        signupForm = findViewById(R.id.signup_form)

        loginTab = findViewById(R.id.login_tab)
        signupTab = findViewById(R.id.signup_tab)

        loginButton = findViewById(R.id.login_button)
        signupButton = findViewById(R.id.signup_button)

        loginTab.setOnClickListener {
            showLoginForm()
        }

        signupTab.setOnClickListener {
            showSignupForm()
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        signupButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        showLoginForm() // Default to login
    }

    private fun showLoginForm() {
        loginForm.visibility = View.VISIBLE
        signupForm.visibility = View.GONE

        // Update tab styles
        loginTab.setBackgroundResource(R.drawable.tab_selected)
        loginTab.setTextColor(ContextCompat.getColor(this, R.color.green_500))

        signupTab.setBackgroundColor(Color.TRANSPARENT)
        signupTab.setTextColor(Color.GRAY)
    }

    private fun showSignupForm() {
        loginForm.visibility = View.GONE
        signupForm.visibility = View.VISIBLE

        // Update tab styles
        signupTab.setBackgroundResource(R.drawable.tab_selected)
        signupTab.setTextColor(ContextCompat.getColor(this, R.color.green_500))

        loginTab.setBackgroundColor(Color.TRANSPARENT)
        loginTab.setTextColor(Color.GRAY)
    }
}
