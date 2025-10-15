package com.stubborndeveloper.databaselearningapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.stubborndeveloper.databaselearningapp.ui.HistoryViewModel // You need to import HistoryViewModel
import com.stubborndeveloper.databaselearningapp.ui.MainQueryViewModel // You likely need to import MainQueryViewModel as well, if it's in the same package

class ViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainQueryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // FIX: Removed the extra HistoryViewModel() argument
            return MainQueryViewModel(application) as T
        }
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistoryViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}