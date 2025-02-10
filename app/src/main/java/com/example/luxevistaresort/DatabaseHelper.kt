package com.example.luxevistaresort

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        // Create Users Table
        val createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT)"
        db.execSQL(createUserTable)

        // Create Bookings Table
        val createBookingTable = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                COLUMN_BOOKING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRAVEL_DATE + " TEXT, " +
                COLUMN_TOTAL_TRAVELERS + " INTEGER, " +
                COLUMN_ROOM_PREFERENCE + " TEXT, " +
                COLUMN_ROOM_TYPE + " TEXT, " +
                COLUMN_INHOUSE_SERVICES + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT)"
        db.execSQL(createBookingTable)

        // Create Promo Events Table
        val createPromoEventTable = "CREATE TABLE " + TABLE_PROMO_EVENTS + " (" +
                COLUMN_EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EVENT_TYPE + " TEXT NOT NULL, " +
                COLUMN_EVENT_NAME + " TEXT NOT NULL, " +
                COLUMN_EVENT_DESCRIPTION + " TEXT NOT NULL)"
        db.execSQL(createPromoEventTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMO_EVENTS)
        onCreate(db)
    }

    // Insert Booking Data
    fun insertBooking(
        travelDate: String?,
        totalTravelers: Int,
        roomPreference: String?,
        roomType: String?,
        inhouseServices: String?,
        userEmail: String?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_TRAVEL_DATE, travelDate)
        values.put(COLUMN_TOTAL_TRAVELERS, totalTravelers)
        values.put(COLUMN_ROOM_PREFERENCE, roomPreference)
        values.put(COLUMN_ROOM_TYPE, roomType)
        values.put(COLUMN_INHOUSE_SERVICES, inhouseServices)
        values.put(COLUMN_USER_EMAIL, userEmail)
        return db.insert(TABLE_BOOKINGS, null, values)
    }

    // Insert Promo Event
    fun insertPromoEvent(type: String?, name: String?, description: String?): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_EVENT_TYPE, type)
        values.put(COLUMN_EVENT_NAME, name)
        values.put(COLUMN_EVENT_DESCRIPTION, description)
        return db.insert(TABLE_PROMO_EVENTS, null, values)
    }

    // Insert User
    fun insertUser(name: String?, email: String?, password: String?): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, name)
        values.put(COLUMN_EMAIL, email)
        values.put(COLUMN_PASSWORD, password)
        return db.insert(TABLE_USERS, null, values)
    }

    // Check User Credentials
    fun checkUser(email: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?",
            arrayOf(email, password)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Fetch Booking History
    @SuppressLint("Range")
    fun getBookingHistory(userEmail: String): List<String> {
        val bookingList: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT " + COLUMN_TRAVEL_DATE + ", " + COLUMN_TOTAL_TRAVELERS + ", " + COLUMN_ROOM_PREFERENCE + ", " + COLUMN_ROOM_TYPE + ", " + COLUMN_INHOUSE_SERVICES + " FROM " + TABLE_BOOKINGS + " WHERE " + COLUMN_USER_EMAIL + " = ?",
            arrayOf(userEmail)
        )

        if (cursor.moveToFirst()) {
            do {
                val travelDate = cursor.getString(cursor.getColumnIndex(COLUMN_TRAVEL_DATE))
                val totalTravelers = cursor.getInt(cursor.getColumnIndex(COLUMN_TOTAL_TRAVELERS))
                val roomPreference = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_PREFERENCE))
                val roomType = cursor.getString(cursor.getColumnIndex(COLUMN_ROOM_TYPE))
                val inhouseServices =
                    cursor.getString(cursor.getColumnIndex(COLUMN_INHOUSE_SERVICES))

                val bookingDetails =
                    "Date: $travelDate\nTravelers: $totalTravelers\nRoom: $roomPreference $roomType\nServices: $inhouseServices"
                bookingList.add(bookingDetails)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return bookingList
    }

    @SuppressLint("Range")
    fun getPromoEvents(): List<String> {
        val promoEventList: MutableList<String> = ArrayList()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT $COLUMN_EVENT_NAME, $COLUMN_EVENT_DESCRIPTION FROM $TABLE_PROMO_EVENTS", null
        )

        if (cursor.moveToFirst()) {
            do {
                val eventName = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_NAME))
                val eventDescription = cursor.getString(cursor.getColumnIndex(COLUMN_EVENT_DESCRIPTION))

                val promoEventDetails = "Event: $eventName\nDescription: $eventDescription"
                promoEventList.add(promoEventDetails)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return promoEventList
    }


    companion object {
        private const val DATABASE_NAME = "luxevista.db"
        private const val DATABASE_VERSION = 3

        // Table Names
        const val TABLE_USERS: String = "users"
        const val TABLE_BOOKINGS: String = "booking_tbl"
        const val TABLE_PROMO_EVENTS: String = "promo_event_tbl"

        // Users Table Columns
        const val COLUMN_ID: String = "id"
        const val COLUMN_NAME: String = "name"
        const val COLUMN_EMAIL: String = "email"
        const val COLUMN_PASSWORD: String = "password"

        // Booking Table Columns
        const val COLUMN_BOOKING_ID: String = "booking_id"
        const val COLUMN_TRAVEL_DATE: String = "travel_date"
        const val COLUMN_TOTAL_TRAVELERS: String = "total_travelers"
        const val COLUMN_ROOM_PREFERENCE: String = "room_preference"
        const val COLUMN_ROOM_TYPE: String = "room_type"
        const val COLUMN_INHOUSE_SERVICES: String = "inhouse_services"
        const val COLUMN_USER_EMAIL: String = "user_email"

        // Promo Events Table Columns
        const val COLUMN_EVENT_ID: String = "event_id"
        const val COLUMN_EVENT_TYPE: String = "type"
        const val COLUMN_EVENT_NAME: String = "event_name"
        const val COLUMN_EVENT_DESCRIPTION: String = "description"
    }
}