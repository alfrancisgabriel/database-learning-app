package com.stubborndeveloper.databaselearningapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.stubborndeveloper.databaselearningapp.R

@Composable
fun CreateDatabaseDialog(
    onDismissRequest: () -> Unit,
    onCreate: (String) -> Unit
) {
    var databaseName by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismissRequest) {
        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.create_your_first_database))
                OutlinedTextField(
                    value = databaseName,
                    onValueChange = { databaseName = it },
                    label = { Text(text = stringResource(id = R.string.database_name)) },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onDismissRequest,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = stringResource(id = R.string.cancel))
                    }
                    Button(
                        onClick = { onCreate(databaseName) },
                        enabled = databaseName.isNotBlank()
                    ) {
                        Text(text = stringResource(id = R.string.create))
                    }
                }
            }
        }
    }
}