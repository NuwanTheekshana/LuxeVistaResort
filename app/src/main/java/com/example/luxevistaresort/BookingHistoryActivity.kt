package com.example.luxevistaresort

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class BookingHistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listViewBookings: ListView
    private lateinit var bookingAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_history)

        dbHelper = DatabaseHelper(this)
        listViewBookings = findViewById(R.id.listViewBookings)

        val sharedPreferences: SharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val loggedInUsername = sharedPreferences.getString("username", null)

        if (loggedInUsername != null) {
            val bookings = dbHelper.getBookingHistory(loggedInUsername)

            bookingAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bookings)
            listViewBookings.adapter = bookingAdapter
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }

        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}