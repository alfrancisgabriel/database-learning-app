package com.stubborndeveloper.databaselearningapp.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

object DatabaseManager {
    private val openHelpers = mutableMapOf<String, SQLiteOpenHelper>()

    fun getHelper(context: Context, dbName: String): SQLiteOpenHelper {
        return openHelpers.getOrPut(dbName) {
            DatabaseHelper(context, dbName)
        }
    }

    fun closeDatabase(dbName: String) {
        openHelpers.remove(dbName)?.close()
    }

    private class DatabaseHelper(context: Context, dbName: String) :
        SQLiteOpenHelper(context, dbName, null, 1) {

        override fun onCreate(db: SQLiteDatabase?) {
            // No-op. The user is expected to create tables via SQL queries.
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            // No-op for this version.
        }
    }
}