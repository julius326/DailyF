package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.abcd.data.HabitViewModel
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_MOTIVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(navController: NavHostController, viewModel: HabitViewModel = viewModel()) {
    var habitText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add Habits", color = Color.White, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    Row {
                        IconButton(onClick = {
                            navController.popBackStack() // Back action
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            navController.navigate(ROUTE_HOME) // Navigate to Home
                        }) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor =  Color(0xFF4B0082) // ✅ same as other screen
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B0082)) { // ✅ same deep purple as HabitListScreen
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
                            tint = Color.White
                        )
                    },
                    label = { Text("Home", color = Color.White) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_HABITLIST) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Habits",
                            tint = Color.White
                        )
                    },
                    label = { Text("Habits", color = Color.White) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_MOTIVATION) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Motivations",
                            tint = Color.White
                        )
                    },
                    label = { Text("Motivations", color = Color.White) }
                )
            }
        },
        containerColor = Color(0xFFADD8E6) // ✅ background matches other screen
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(Color(0xFFADD8E6)) // ✅ consistent background
        ) {
            OutlinedTextField(
                value = habitText,
                onValueChange = { habitText = it },
                label = { Text("Enter habit") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (habitText.isNotBlank()) {
                    viewModel.addHabit(
                        text = habitText,
                        onSuccess = {
                            habitText = ""
                            navController.popBackStack() // Go back to list
                        },
                        onError = {
                            error = it
                        }
                    )
                } else {
                    error = "Habit can't be empty"
                }
            }) {
                Text("Save Habit")
            }

            error?.let {
                Text(text = it, color = Color.Red)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AddHabitScreenPreview() {
    HabitScreen(rememberNavController())
}
