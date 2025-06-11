package com.example.becycle.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat // Import this
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.becycle.R
import com.google.android.material.navigation.NavigationView
import com.example.becycle.utils.UserPreference

open class BaseActivity : AppCompatActivity() {

    protected lateinit var userPreference: UserPreference
    private var currentSelectedNavId: Int = -1

    // Map navigation IDs to a numerical position (e.g., 0 for Home, 1 for History, 2 for Article)
    private val navItemPositions = mapOf(
        R.id.bottom_nav_home to 0,
        R.id.bottom_nav_history to 1,
        R.id.bottom_nav_article to 2
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.apply {
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    )
            statusBarColor = android.graphics.Color.TRANSPARENT
        }

        window.decorView.setOnApplyWindowInsetsListener { v, insets ->
            val content = findViewById<View>(android.R.id.content)
            content.setPadding(
                content.paddingLeft,
                insets.systemWindowInsetTop,
                content.paddingRight,
                content.paddingBottom
            )
            insets.consumeSystemWindowInsets()
        }

        userPreference = UserPreference(this)
    }

    override fun onStart() {
        super.onStart()
        highlightBottomNavItemForCurrentActivity()
    }

    open fun showBottomNav(): Boolean = true

    fun setupBottomNavIfNeeded() {
        if (!showBottomNav()) return

        val homeNav = findViewById<LinearLayout>(R.id.bottom_nav_home)
        val historyNav = findViewById<LinearLayout>(R.id.bottom_nav_history)
        val articleNav = findViewById<LinearLayout>(R.id.bottom_nav_article)

        homeNav?.setOnClickListener {
            handleBottomNavClick(R.id.bottom_nav_home, HomeActivity::class.java)
        }

        historyNav?.setOnClickListener {
            handleBottomNavClick(R.id.bottom_nav_history, HistoryActivity::class.java)
        }

        articleNav?.setOnClickListener {
            handleBottomNavClick(R.id.bottom_nav_article, ArticleActivity::class.java)
        }
    }

    // New helper to handle navigation and transitions
    private fun handleBottomNavClick(targetNavId: Int, targetActivityClass: Class<out AppCompatActivity>) {
        if (currentSelectedNavId == targetNavId) {
            return // Already on this tab
        }

        val currentPosition = navItemPositions[currentSelectedNavId] ?: -1
        val targetPosition = navItemPositions[targetNavId] ?: -1

        val enterAnim: Int
        val exitAnim: Int

        if (targetPosition > currentPosition) { // Moving right (e.g., Home -> History, History -> Article)
            enterAnim = R.anim.slide_in_right
            exitAnim = R.anim.slide_out_left
        } else { // Moving left (e.g., Article -> History, History -> Home)
            enterAnim = R.anim.slide_in_left
            exitAnim = R.anim.slide_out_right
        }

        setBottomNavItemState(targetNavId)

        val intent = Intent(this, targetActivityClass)
        // Create custom animation options
        val options = ActivityOptionsCompat.makeCustomAnimation(this, enterAnim, exitAnim)
        startActivity(intent, options.toBundle())

        finish() // Finish current activity to prevent stack build-up
    }

    private fun setBottomNavItemState(selectedId: Int) {
        val homeNav = findViewById<LinearLayout>(R.id.bottom_nav_home)
        val historyNav = findViewById<LinearLayout>(R.id.bottom_nav_history)
        val articleNav = findViewById<LinearLayout>(R.id.bottom_nav_article)

        // Reset all to unselected
        homeNav?.isSelected = false
        historyNav?.isSelected = false
        articleNav?.isSelected = false

        homeNav?.findViewById<ImageView>(R.id.icon_home)?.isSelected = false
        historyNav?.findViewById<ImageView>(R.id.icon_history)?.isSelected = false
        articleNav?.findViewById<ImageView>(R.id.icon_article)?.isSelected = false

        // Set the selected one
        when (selectedId) {
            R.id.bottom_nav_home -> {
                homeNav?.isSelected = true
                homeNav?.findViewById<ImageView>(R.id.icon_home)?.isSelected = true
            }
            R.id.bottom_nav_history -> {
                historyNav?.isSelected = true
                historyNav?.findViewById<ImageView>(R.id.icon_history)?.isSelected = true
            }
            R.id.bottom_nav_article -> {
                articleNav?.isSelected = true
                articleNav?.findViewById<ImageView>(R.id.icon_article)?.isSelected = true
            }
        }
        currentSelectedNavId = selectedId // Update tracking variable
    }

    private fun highlightBottomNavItemForCurrentActivity() {
        // Determine the current activity's nav ID and update currentSelectedNavId
        currentSelectedNavId = when (this) {
            is HomeActivity -> R.id.bottom_nav_home
            is HistoryActivity -> R.id.bottom_nav_history
            is ArticleActivity -> R.id.bottom_nav_article
            else -> -1 // No item selected for other activities
        }
        if (currentSelectedNavId != -1) {
            setBottomNavItemState(currentSelectedNavId)
        }
    }

    override fun setContentView(layoutResID: Int) {
        val fullView = layoutInflater.inflate(R.layout.activity_drawer, null)
        val contentFrame = fullView.findViewById<FrameLayout>(R.id.content_frame)
        layoutInflater.inflate(layoutResID, contentFrame, true)
        super.setContentView(fullView)

        val menuIcon = findViewById<View>(R.id.menu_icon)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)

        menuIcon?.setOnClickListener {
            drawerLayout?.openDrawer(GravityCompat.START)
        }

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    userPreference.clearAuthTokens()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }

        // Call highlightBottomNavItemForCurrentActivity here to set initial state
        // This makes sure the correct tab is highlighted right after setContentView
        highlightBottomNavItemForCurrentActivity()

        // setupBottomNavIfNeeded() must be called after views are inflated and currentSelectedNavId is set
        setupBottomNavIfNeeded()
    }
}