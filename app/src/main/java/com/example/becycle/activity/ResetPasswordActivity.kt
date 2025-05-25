package com.example.becycle.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.becycle.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ResetPasswordActivity : BaseActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val emailInput = findViewById<EditText>(R.id.email_input)
        val resetButton = findViewById<Button>(R.id.reset_button)

        resetButton.setOnClickListener {
            val email = emailInput.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sendPasswordResetEmail(email)
        }
    }

    private fun sendPasswordResetEmail(email: String) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonBody = """
            {
                "email": "$email"
            }
        """.trimIndent()
        val body = jsonBody.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://project-ppl-production.up.railway.app/auth/forgot-password")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ResetPasswordActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        val intent = Intent(this@ResetPasswordActivity, EmailStatusActivity::class.java)
                        intent.putExtra("is_reset", true)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@ResetPasswordActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
