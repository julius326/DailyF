package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_MOTIVATION

// Sample task data class
data class TodayTask(
    val task: String,
    val time: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayTaskScreen(navController: NavHostController) {
    // Sample data
    val taskList = listOf(
        TodayTask("Buy groceries", "9:00 AM"),
        TodayTask("Team meeting", "11:30 AM"),
        TodayTask("Workout", "5:00 PM"),
        TodayTask("Read a book", "8:00 PM")
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Today's Tasks", color = Color.Blue) },
                navigationIcon = {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            navController.navigate(ROUTE_HOME) // Navigate to Home
                        }) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = Color.Blue
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_HOME){
                        popUpTo (ROUTE_HOME){inclusive = true}
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.Blue
                        )
                    },
                    label = { Text("Home", color = Color.Blue) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_HABITLIST){
                            popUpTo (ROUTE_HOME){ inclusive = true }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Habits",
                            tint = Color.Blue
                        )
                    },
                    label = { Text("Habits", color = Color.Blue) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_MOTIVATION){
                            popUpTo (ROUTE_HOME){ inclusive = true }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Motivations",
                            tint = Color.Blue
                        )
                    },
                    label = { Text("Motivations", color = Color.Blue) }
                )
            }
        },
        containerColor = Color.Blue
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Table Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("#", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(0.1f))
                Text("Task", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(0.6f))
                Text("Time", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(0.3f))
            }

            Divider(color = Color.White, thickness = 1.dp)

            // Table Rows
            LazyColumn {
                itemsIndexed(taskList) { index, task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${index + 1}", color = Color.White, modifier = Modifier.weight(0.1f))
                        Text(task.task, color = Color.White, modifier = Modifier.weight(0.6f))
                        Text(task.time, color = Color.White, modifier = Modifier.weight(0.3f))
                    }
                    Divider(color = Color.White.copy(alpha = 0.3f))
                }
            }
        }
    }
}
