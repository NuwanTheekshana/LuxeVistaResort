package com.example.luxevistaresort

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home)

        // Initialize DrawerLayout and NavigationView
        drawerLayout = findViewById(R.id.home)
        navigationView = findViewById(R.id.navigation_view)

        // Setup the toolbar as the action bar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Setup the toggle button for opening/closing the drawer
        val actionBarDrawerToggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // Set up the navigation item selection listener
        navigationView.setNavigationItemSelectedListener { menuItem ->
            handleNavigationItemSelected(menuItem)
            true
        }

        // Get the logged-in username from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val loggedInUsername = sharedPreferences.getString("username", null)

        if (loggedInUsername != null) {
            // Username is retrieved, you can use it for display or other purposes
            Toast.makeText(this, "Logged in as: $loggedInUsername", Toast.LENGTH_SHORT).show()

            // Conditionally show/hide the "Add Promotion / Events" menu item based on username
            val addPromotionMenuItem = navigationView.menu.findItem(R.id.nav_add_promotion_events)
            if (loggedInUsername == "admin@gmail.com") {
                addPromotionMenuItem.isVisible = true
            } else {
                addPromotionMenuItem.isVisible = false
            }
        } else {
            // Handle the case where no user is logged in
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleNavigationItemSelected(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.nav_add_booking -> {
                val intent = Intent(this, AddBookingActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_booking_history -> {
                val intent = Intent(this, BookingHistoryActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_add_promotion_events -> {
                val intent = Intent(this, AddPromoEventActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_view_promotion_events -> {
                val intent = Intent(this, ViewPromoEventActivity::class.java)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START) // Close the drawer when an item is selected
    }
}
