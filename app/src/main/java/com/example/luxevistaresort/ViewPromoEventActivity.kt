package com.example.luxevistaresort

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ViewPromoEventActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listViewPromoEvents: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_promo_event)

        dbHelper = DatabaseHelper(this)

        listViewPromoEvents = findViewById(R.id.listViewPromoEvents)

        val promoEvents = dbHelper.getPromoEvents()

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, promoEvents)

        listViewPromoEvents.adapter = adapter

        val btnBack = findViewById<ImageView>(R.id.btn_back_view_promo)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}