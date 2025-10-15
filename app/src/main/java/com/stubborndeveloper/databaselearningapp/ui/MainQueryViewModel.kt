package com.stubborndeveloper.databaselearningapp.ui

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stubborndeveloper.databaselearningapp.data.DatabaseManager
import com.stubborndeveloper.databaselearningapp.util.SyntaxHighlighter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainQueryViewModel(application: Application) : AndroidViewModel(application) {
    private val _sqlQuery = MutableStateFlow(TextFieldValue(""))
    val sqlQuery = _sqlQuery.asStateFlow()

    private val _queryResults = MutableStateFlow<List<List<String>>?>(null)
    val queryResults = _queryResults.asStateFlow()

    private val _schema = MutableStateFlow<Map<String, List<String>>?>(null)
    val schema = _schema.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage = _snackbarMessage.asStateFlow()

    private var dbHelper: SQLiteOpenHelper? = null
    private var dbName: String? = null

    fun init(databaseName: String) {
        dbName = databaseName
        dbHelper = DatabaseManager.getHelper(getApplication(), "$databaseName.sqlite")
        fetchSchema()
    }

    fun onSqlQueryChange(newQuery: TextFieldValue) {
        _sqlQuery.value = newQuery.copy(
            annotatedString = SyntaxHighlighter.highlight(newQuery.text)
        )
    }

    fun executeQuery() {
        viewModelScope.launch {
            _errorMessage.value = null
            _queryResults.value = null

            val query = _sqlQuery.value.text.trim()
            if (query.isBlank()) {
                _errorMessage.value = "Query is empty"
                return@launch
            }

            try {
                if (query.startsWith("select", ignoreCase = true)) {
                    executeSelectQuery(query)
                } else {
                    executeDmlDdlQuery(query)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            }
        }
    }

    private fun executeSelectQuery(query: String) {
        val db = dbHelper?.readableDatabase ?: throw IllegalStateException("Database not initialized")
        db.rawQuery(query, null).use { cursor ->
            val columnNames = cursor.columnNames.toList()
            val results = mutableListOf<List<String>>()
            results.add(columnNames)
            while (cursor.moveToNext()) {
                val row = mutableListOf<String>()
                for (i in columnNames.indices) {
                    row.add(cursor.getString(i) ?: "NULL")
                }
                results.add(row)
            }
            _queryResults.value = results
            _snackbarMessage.value = "${results.size - 1} rows returned"
        }
    }

    private fun executeDmlDdlQuery(query: String) {
        val db = dbHelper?.writableDatabase ?: throw IllegalStateException("Database not initialized")
        db.execSQL(query)
        _snackbarMessage.value = "Query executed successfully"
        fetchSchema()
    }

    fun fetchSchema() {
        viewModelScope.launch {
            val db = dbHelper?.readableDatabase ?: return@launch
            val schemaMap = mutableMapOf<String, List<String>>()
            db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'android_%' AND name NOT LIKE 'sqlite_%'", null).use { tableCursor ->
                if (tableCursor.moveToFirst()) {
                    do {
                        val tableName = tableCursor.getString(0)
                        val columnList = mutableListOf<String>()
                        db.rawQuery("PRAGMA table_info($tableName)", null).use { columnCursor ->
                            if (columnCursor.moveToFirst()) {
                                do {
                                    val colName = columnCursor.getString(1)
                                    val colType = columnCursor.getString(2)
                                    columnList.add("$colName: $colType")
                                } while (columnCursor.moveToNext())
                            }
                        }
                        schemaMap[tableName] = columnList
                    } while (tableCursor.moveToNext())
                }
            }
            _schema.value = schemaMap
        }
    }

    fun onSnackbarMessageShown() {
        _snackbarMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        dbName?.let { DatabaseManager.closeDatabase(it) }
    }
}