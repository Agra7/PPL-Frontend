package com.example.becycle.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.becycle.R

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

    open fun showBottomNav(): Boolean = true  // default to showing it

    fun setupBottomNavIfNeeded() {
        if (!showBottomNav()) return

        val homeButton = findViewById<LinearLayout>(R.id.bottom_nav_home)
        val historyButton = findViewById<LinearLayout>(R.id.bottom_nav_history)
        val articleButton = findViewById<LinearLayout>(R.id.bottom_nav_article)

        homeButton?.setOnClickListener {
            if (this !is HomeActivity) {
                startActivity(Intent(this, HomeActivity::class.java))
            }
        }

        historyButton?.setOnClickListener {
            if (this !is HistoryActivity) {
                startActivity(Intent(this, HistoryActivity::class.java))
            }
        }

        articleButton?.setOnClickListener {
            if (this !is ArticleActivity) {
                startActivity(Intent(this, ArticleActivity::class.java))
            }
        }
    }

    override fun setContentView(layoutResID: Int) {
        val fullView = layoutInflater.inflate(R.layout.activity_drawer, null)
        val contentFrame = fullView.findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResID, contentFrame, true)
        super.setContentView(fullView)

        // Setup drawer toggle click (burger icon)
        val menuIcon = findViewById<View>(R.id.menu_icon)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        menuIcon?.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }

        setupBottomNavIfNeeded()
    }

}
