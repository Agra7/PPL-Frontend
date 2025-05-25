package com.example.becycle.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.becycle.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ChangePasswordActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        val newPasswordInput = findViewById<EditText>(R.id.new_password_input)
        val submitButton = findViewById<Button>(R.id.submit_button)

        val token = intent.data?.lastPathSegment  // extract token from deep link

        if (token == null) {
            Toast.makeText(this, "Invalid or missing reset token", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        submitButton.setOnClickListener {
            val newPassword = newPasswordInput.text.toString()
            if (newPassword.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            resetPassword(token, newPassword)
        }
    }

    private fun resetPassword(token: String, newPassword: String) {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val jsonBody = """
            {
                "password": "$newPassword"
            }
        """.trimIndent()
        val body = jsonBody.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("https://project-ppl-production.up.railway.app/auth/reset-password/$token")
            .post(body)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ChangePasswordActivity, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@ChangePasswordActivity, "Password reset successful. Please login.", Toast.LENGTH_LONG).show()
                        finish() // close this screen and maybe redirect to login
                    } else {
                        Toast.makeText(this@ChangePasswordActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
