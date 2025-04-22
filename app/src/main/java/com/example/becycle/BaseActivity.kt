package com.example.becycle

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import android.graphics.Color

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the status bar transparent
        window.apply {
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
            statusBarColor = android.graphics.Color.TRANSPARENT
        }

        // Add padding to top view after content is set
        window.decorView.setOnApplyWindowInsetsListener { v, insets ->
            val content = findViewById<View>(android.R.id.content)
            content.setPadding(
                content.paddingLeft,
                insets.systemWindowInsetTop, // top = status bar height
                content.paddingRight,
                content.paddingBottom
            )
            insets.consumeSystemWindowInsets()
        }
    }
}
