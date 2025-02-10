package com.example.luxevistaresort

import android.app.DatePickerDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddBookingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_booking)

        dbHelper = DatabaseHelper(this)

        // Initialize the back button
        val btnBack = findViewById<ImageView>(R.id.btn_back)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val etTravelDate = findViewById<EditText>(R.id.et_travel_date)
        val etTotalTravelers = findViewById<EditText>(R.id.et_total_travelers)
        val spinnerRoomPreference = findViewById<Spinner>(R.id.spinner_room_preference)
        val spinnerRoomType = findViewById<Spinner>(R.id.spinner_room_type)
        val checkBoxSpa = findViewById<CheckBox>(R.id.check_spa)
        val checkBoxCabanas = findViewById<CheckBox>(R.id.check_cabanas)
        val checkBoxDining = findViewById<CheckBox>(R.id.check_dining)
        val btnSubmitBooking = findViewById<Button>(R.id.btn_submit_booking)

        etTravelDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                    etTravelDate.setText(selectedDate)
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        btnSubmitBooking.setOnClickListener {
            val travelDate = etTravelDate.text.toString()
            val totalTravelersInput = etTotalTravelers.text.toString()

            val totalTravelers = if (totalTravelersInput.isNotEmpty()) {
                totalTravelersInput.toIntOrNull() ?: 0
            } else {
                0
            }

            val roomPreference = spinnerRoomPreference.selectedItem.toString()
            val roomType = spinnerRoomType.selectedItem.toString()
            val inhouseServices = buildInhouseServices(checkBoxSpa, checkBoxCabanas, checkBoxDining)

            val sharedPreferences: SharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
            val loggedInUsername = sharedPreferences.getString("username", null)

            if (loggedInUsername != null) {
                if (travelDate.isEmpty() || totalTravelers <= 0) {
                    Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                } else {
                    val bookingId = dbHelper.insertBooking(
                        travelDate,
                        totalTravelers,
                        roomPreference,
                        roomType,
                        inhouseServices,
                        loggedInUsername
                    )

                    if (bookingId != -1L) {
                        Toast.makeText(this, "Booking Submitted", Toast.LENGTH_SHORT).show()

                        // Clear the input fields after successful submission
                        etTravelDate.setText("")
                        etTotalTravelers.setText("")
                    } else {
                        Toast.makeText(this, "Failed to submit booking", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun buildInhouseServices(
        spa: CheckBox,
        cabanas: CheckBox,
        dining: CheckBox
    ): String {
        val services = mutableListOf<String>()
        if (spa.isChecked) services.add("Spa")
        if (cabanas.isChecked) services.add("Poolside Cabanas")
        if (dining.isChecked) services.add("Dining Reservations")
        return services.joinToString(", ")
    }
}
