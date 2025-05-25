package com.example.becycle.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.example.becycle.R

class EmailStatusActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_status)

        // Reference the views
        val title = findViewById<TextView>(R.id.title)
        val subtitle = findViewById<TextView>(R.id.subtitle)
        val emailIcon = findViewById<ImageView>(R.id.email_icon)
        val description = findViewById<TextView>(R.id.description)
        val resendButton = findViewById<Button>(R.id.resend_button)
        val closeButton = findViewById<ImageButton>(R.id.close_button)

        // Close button logic
        closeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Check whether it's a password reset or signup
        val isReset = intent.getBooleanExtra("is_reset", false)

        if (isReset) {
            title.text = "Check Your Email!"
            subtitle.text = "We’ve sent you a link to reset your password."
            emailIcon.setImageResource(R.drawable.ic_email_key)
            description.text = "Please open your email and click the password reset link. Once you’ve set a new password, you’ll be able to log back into the app."
        } else {
            title.text = "Check Your Email!"
            subtitle.text = "We’ve sent a verification link to your email."
            emailIcon.setImageResource(R.drawable.ic_email_check)
            description.text = "To complete your sign-up process, please open your email and click the verification link we sent you. Once verified, you can return to the app and start using your account."
        }

        // Optionally handle Resend button
        resendButton.setOnClickListener {
            // TODO: Add resend logic when backend is ready
        }
    }
}
