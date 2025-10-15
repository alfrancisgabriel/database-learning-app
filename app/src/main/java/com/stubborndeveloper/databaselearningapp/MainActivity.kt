package com.stubborndeveloper.databaselearningapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stubborndeveloper.databaselearningapp.ui.DatabaseManagementScreen
import com.stubborndeveloper.databaselearningapp.ui.MainQueryScreen
import com.stubborndeveloper.databaselearningapp.ui.theme.DatabaseLearningAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DatabaseLearningAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "database_management",
        modifier = modifier
    ) {
        composable("database_management") {
            DatabaseManagementScreen(
                onDatabaseSelected = { dbName ->
                    navController.navigate("main_query/$dbName")
                }
            )
        }
        composable("main_query/{databaseName}") { backStackEntry ->
            val databaseName = backStackEntry.arguments?.getString("databaseName") ?: ""
            MainQueryScreen(databaseName = databaseName)
        }
    }
}