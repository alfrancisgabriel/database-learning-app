package com.stubborndeveloper.databaselearningapp.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HistoryViewModel : ViewModel() {
    private val _queryHistory = MutableStateFlow<List<String>>(emptyList())
    val queryHistory = _queryHistory.asStateFlow()

    fun addQueryToHistory(query: String) {
        val currentHistory = _queryHistory.value.toMutableList()
        currentHistory.add(0, query)
        _queryHistory.value = currentHistory
    }
}