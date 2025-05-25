package com.example.becycle.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import com.example.becycle.R
import com.example.becycle.model.AuthResponse
import com.example.becycle.network.ApiClient
import com.example.becycle.network.AuthService
import com.example.becycle.network.LoginRequest
import com.example.becycle.network.RegisterRequest
import com.example.becycle.utils.JwtUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity() {

    private lateinit var loginForm: LinearLayout
    private lateinit var signupForm: LinearLayout
    private lateinit var loginTab: TextView
    private lateinit var signupTab: TextView
    private lateinit var loginButton: Button
    private lateinit var signupButton: Button
    private lateinit var forgotPassword: TextView

    private lateinit var loginEmailInput: EditText
    private lateinit var loginPasswordInput: EditText
    private lateinit var signupUsernameInput: EditText
    private lateinit var signupEmailInput: EditText
    private lateinit var signupPasswordInput: EditText
    private lateinit var signupConfirmPasswordInput: EditText

    // New: Google Sign-In client
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    private val authService by lazy { ApiClient.retrofit.create(AuthService::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Color.TRANSPARENT
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        setContentView(R.layout.activity_main)

        loginForm = findViewById(R.id.login_form)
        signupForm = findViewById(R.id.signup_form)

        loginTab = findViewById(R.id.login_tab)
        signupTab = findViewById(R.id.signup_tab)

        loginButton = findViewById(R.id.login_button)
        signupButton = findViewById(R.id.signup_button)
        forgotPassword = findViewById(R.id.forgot_password)

        loginEmailInput = findViewById(R.id.email_username_login)
        loginPasswordInput = findViewById(R.id.password_login)
        signupUsernameInput = findViewById(R.id.username_signup)
        signupEmailInput = findViewById(R.id.email_signup)
        signupPasswordInput = findViewById(R.id.password_signup)
        signupConfirmPasswordInput = findViewById(R.id.confirm_password)

        // Google Sign-In buttons from your XML (make sure you added IDs to them)
        val googleLoginBtn = findViewById<ImageButton>(R.id.google_login_button)
        val googleSignupBtn = findViewById<ImageButton>(R.id.google_signup_button)

        // Google Sign-In options setup
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            // .requestIdToken(getString(R.string.your_web_client_id)) // Uncomment if you use ID tokens with backend
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set click listeners for Google sign-in buttons
        googleLoginBtn?.setOnClickListener { signInWithGoogle() }
        googleSignupBtn?.setOnClickListener { signInWithGoogle() }

        loginTab.setOnClickListener {
            showLoginForm()
        }

        signupTab.setOnClickListener {
            showSignupForm()
        }

        loginButton.setOnClickListener {
            performLogin()
        }

        signupButton.setOnClickListener {
            performSignup()
        }

        forgotPassword.setOnClickListener {
            val intent = Intent(this, ResetPasswordActivity::class.java)
            intent.putExtra("is_reset", true) // Password reset mode
            startActivity(intent)
        }

        showLoginForm() // Default to login
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Successfully signed in
            Toast.makeText(this, "Google Sign-In success: ${account?.email}", Toast.LENGTH_SHORT).show()

            // TODO: Send account.idToken or account info to your backend for authentication
            // For example:
            // val idToken = account?.idToken
            // sendIdTokenToBackend(idToken)

        } catch (e: ApiException) {
            Toast.makeText(this, "Google Sign-In failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun performLogin() {
        val emailOrUsername = loginEmailInput.text.toString().trim()
        val password = loginPasswordInput.text.toString().trim()

        if (emailOrUsername.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all login fields", Toast.LENGTH_SHORT).show()
            return
        }

        val request = LoginRequest(emailOrUsername, password)
        authService.login(request).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    // Extract user_id from JWT
                    val userId = JwtUtils.decodeUserId(authResponse.accessToken).also {
                        Log.d("JWT", "Extracted user_id: $it")
                    } ?: run {
                        Toast.makeText(this@MainActivity, "Failed to extract user ID", Toast.LENGTH_SHORT).show()
                        return
                    }

                    getSharedPreferences("auth", MODE_PRIVATE).edit()
                        .putString("access_token", authResponse.accessToken)
                        .putString("refresh_token", authResponse.refreshToken)
                        .putString("user_id", userId)
                        .apply()

                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    finish()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun performSignup() {
        val username = signupUsernameInput.text.toString().trim()
        val email = signupEmailInput.text.toString().trim()
        val password = signupPasswordInput.text.toString().trim()
        val confirmPassword = signupConfirmPasswordInput.text.toString().trim()

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill all signup fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        val request = RegisterRequest(username, email, password)
        authService.register(request).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    val intent = Intent(this@MainActivity, EmailStatusActivity::class.java)
                    intent.putExtra("is_reset", false) // Email verification mode
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "Signup failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showLoginForm() {
        loginForm.visibility = View.VISIBLE
        signupForm.visibility = View.GONE

        loginTab.setBackgroundResource(R.drawable.tab_selected)
        loginTab.setTextColor(ContextCompat.getColor(this, R.color.green_500))

        signupTab.setBackgroundColor(Color.TRANSPARENT)
        signupTab.setTextColor(Color.GRAY)
    }

    private fun showSignupForm() {
        loginForm.visibility = View.GONE
        signupForm.visibility = View.VISIBLE

        signupTab.setBackgroundResource(R.drawable.tab_selected)
        signupTab.setTextColor(ContextCompat.getColor(this, R.color.green_500))

        loginTab.setBackgroundColor(Color.TRANSPARENT)
        loginTab.setTextColor(Color.GRAY)
    }
}
