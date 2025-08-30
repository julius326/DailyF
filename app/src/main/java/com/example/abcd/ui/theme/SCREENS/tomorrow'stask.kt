package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_MOTIVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TomorrowTaskScreen(navController: NavHostController) {
    var taskText by remember { mutableStateOf("") }
    var taskAdded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tomorrow's Task", color = Color.Blue) },
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
        bottomBar = @androidx.compose.runtime.Composable {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_HOME){
                            popUpTo(ROUTE_HOME){inclusive = true}
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
        containerColor = Color.White
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = taskText,
                onValueChange = {
                    taskText = it
                    taskAdded = false // reset message on input change
                },
                label = { Text("Enter task for tomorrow") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (taskText.isNotBlank()) {
                        // Fake adding the task
                        taskAdded = true
                        taskText = ""
                    }
                },
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth()
            ) {
                Text("Add for Tomorrow", fontSize = 18.sp)
            }

            if (taskAdded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Task added!", color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TomorrowTaskScreenPreview() {
    TomorrowTaskScreen(rememberNavController())
}
