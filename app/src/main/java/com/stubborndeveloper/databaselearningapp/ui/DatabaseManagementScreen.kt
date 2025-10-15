package com.stubborndeveloper.databaselearningapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.stubborndeveloper.databaselearningapp.R
import com.stubborndeveloper.databaselearningapp.data.DatabaseManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatabaseManagementScreen(
    modifier: Modifier = Modifier,
    onDatabaseSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var databaseFiles by remember { mutableStateOf<List<File>>(emptyList()) }
    var showCreateDatabaseDialog by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    fun refreshDatabaseFiles() {
        val filesDir = context.filesDir
        val files = filesDir.listFiles { file ->
            file.isFile && file.name.endsWith(".sqlite")
        }?.toList() ?: emptyList()
        databaseFiles = files
    }

    fun createSampleDatabase() {
        val dbName = "sample_database"
        val dbHelper = DatabaseManager.getHelper(context, "$dbName.sqlite")
        val db = dbHelper.writableDatabase

        db.execSQL("CREATE TABLE Customers (id INTEGER PRIMARY KEY, name TEXT, email TEXT)")
        db.execSQL("CREATE TABLE Orders (id INTEGER PRIMARY KEY, customer_id INTEGER, amount REAL, FOREIGN KEY(customer_id) REFERENCES Customers(id))")
        db.execSQL("INSERT INTO Customers (name, email) VALUES ('John Doe', 'john.doe@example.com')")
        db.execSQL("INSERT INTO Customers (name, email) VALUES ('Jane Smith', 'jane.smith@example.com')")
        db.execSQL("INSERT INTO Orders (customer_id, amount) VALUES (1, 100.0)")
        db.execSQL("INSERT INTO Orders (customer_id, amount) VALUES (2, 150.0)")

        refreshDatabaseFiles()
        onDatabaseSelected(dbName)
    }

    LaunchedEffect(Unit) {
        refreshDatabaseFiles()
        if (databaseFiles.isEmpty()) {
            showCreateDatabaseDialog = true
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(painter = painterResource(id = R.drawable.ic_more_vert), contentDescription = "More options")
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = { Text("Create Sample Database") }, onClick = {
                            createSampleDatabase()
                            showMenu = false
                        })
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showCreateDatabaseDialog = true }) {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription = "Create Database")
            }
        }
    ) {
        if (databaseFiles.isEmpty() && !showCreateDatabaseDialog) {
            Box(modifier = Modifier.fillMaxSize().padding(it), contentAlignment = Alignment.Center) {
                Text(text = "No databases found. Create one!")
            }
        } else if (databaseFiles.isNotEmpty()) {
            LazyColumn(modifier = Modifier.padding(it).padding(16.dp)) {
                items(databaseFiles) { file ->
                    DatabaseListItem(
                        file = file,
                        onItemClick = { onDatabaseSelected(file.nameWithoutExtension) }
                    )
                }
            }
        }

    }


    if (showCreateDatabaseDialog) {
        CreateDatabaseDialog(
            onDismissRequest = { showCreateDatabaseDialog = false },
            onCreate = { dbName ->
                val newDbFile = File(context.filesDir, "$dbName.sqlite")
                newDbFile.createNewFile()
                refreshDatabaseFiles()
                showCreateDatabaseDialog = false
                onDatabaseSelected(dbName)
            }
        )
    }
}

@Composable
fun DatabaseListItem(
    file: File,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onItemClick() }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = file.nameWithoutExtension)
            Text(text = "Last modified: ${file.lastModified().toFormattedDateString()}")
        }
    }
}

fun Long.toFormattedDateString(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}