package com.example.luxevistaresort

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddPromoEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_promo_event)

        val sharedPreferences: SharedPreferences = getSharedPreferences("userPrefs", MODE_PRIVATE)
        val loggedInUsername = sharedPreferences.getString("username", null)

        val btnBack = findViewById<ImageView>(R.id.btn_back_promo)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val spinnerType = findViewById<Spinner>(R.id.spinner_promo_event_type)
        val etName = findViewById<EditText>(R.id.et_promo_event_name)
        val etDescription = findViewById<EditText>(R.id.et_promo_event_description)
        val btnSubmit = findViewById<Button>(R.id.btn_submit_booking)

        val dbHelper = DatabaseHelper(this)

        btnSubmit.setOnClickListener {
            val type = spinnerType.selectedItem.toString()
            val name = etName.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val values = ContentValues().apply {
                    put("type", type)
                    put("event_name", name)
                    put("description", description)
                }

                val newRowId = db.insert("promo_event_tbl", null, values)

                if (newRowId != -1L) {
                    Toast.makeText(this, "Promotion/Event added successfully", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etDescription.text.clear()
                } else {
                    Toast.makeText(this, "Error adding Promotion/Event", Toast.LENGTH_SHORT).show()
                }
                db.close()
            }
        }
    }
}
