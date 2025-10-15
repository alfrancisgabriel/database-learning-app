package com.stubborndeveloper.databaselearningapp.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.stubborndeveloper.databaselearningapp.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainQueryScreen(
    databaseName: String,
    viewModel: MainQueryViewModel = viewModel()
) {
    LaunchedEffect(databaseName) {
        viewModel.init(databaseName)
    }

    val sqlQuery by viewModel.sqlQuery.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val queryResults by viewModel.queryResults.collectAsStateWithLifecycle()
    val schema by viewModel.schema.collectAsStateWithLifecycle()

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Data", "Schema")
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onSnackbarMessageShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(it)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = sqlQuery,
                    onValueChange = { viewModel.onSqlQueryChange(it) },
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { viewModel.executeQuery() }) {
                    Icon(painter = painterResource(id = R.drawable.ic_run), contentDescription = "Run SQL")
                }
            }
            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(selected = selectedTabIndex == index,
                            onClick = { selectedTabIndex = index },
                            text = { Text(text = title) })
                    }
                }
                Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    when (selectedTabIndex) {
                        0 -> {
                            if (queryResults != null) {
                                val headers = queryResults!!.first()
                                val data = queryResults!!.drop(1)
                                LazyColumn(modifier = Modifier.fillMaxSize()) {
                                    stickyHeader {
                                        Row(Modifier.fillMaxWidth()) {
                                            headers.forEach {
                                                Text(text = it, modifier = Modifier.weight(1f).padding(8.dp))
                                            }
                                        }
                                    }
                                    items(data) {
                                        Row(Modifier.fillMaxWidth()) {
                                            it.forEach {
                                                Text(text = it, modifier = Modifier.weight(1f).padding(8.dp))
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = "Data will be displayed here.")
                                }
                            }
                        }
                        1 -> {
                            if (schema != null) {
                                LazyColumn {
                                    items(schema!!.entries.toList()) { (tableName, columns) ->
                                        Column(modifier = Modifier.padding(vertical = 8.dp)) {
                                            Text(text = tableName)
                                            columns.forEach {
                                                Text(text = "  $it", modifier = Modifier.padding(start = 8.dp))
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Text(text = "Schema will be displayed here.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}