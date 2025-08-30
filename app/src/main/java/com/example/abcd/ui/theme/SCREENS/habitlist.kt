package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.abcd.data.HabitViewModel
import com.example.abcd.models.Habit
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_HABITS
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_MOTIVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    navController: NavHostController,
    viewModel: HabitViewModel = viewModel()
) {
    val habitList by viewModel.habits.collectAsState()
    var error by remember { mutableStateOf<String?>(null) }

    // State for which habit is being edited and the updated text
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var updatedText by remember { mutableStateOf("") }

    // Fetch habits on launch
    LaunchedEffect(Unit) {
        viewModel.fetchHabits {
            error = it
        }
    }

    // Dialog for updating habit
    if (habitToEdit != null) {
        AlertDialog(
            onDismissRequest = { habitToEdit = null },
            title = { Text("Update Habit") },
            text = {
                TextField(
                    value = updatedText,
                    onValueChange = { updatedText = it },
                    label = { Text("Habit") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (updatedText.isNotBlank()) {
                        viewModel.updateHabit(
                            habitToEdit!!.id,
                            updatedText,
                            onSuccess = {
                                habitToEdit = null
                                updatedText = ""
                                viewModel.fetchHabits { error = it }
                            },
                            onError = { error = it }
                        )
                    }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { habitToEdit = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = @androidx.compose.runtime.Composable {
            CenterAlignedTopAppBar(
                title = {
                    Text("Habits", color = Color.Blue, fontWeight = FontWeight.Bold)
                },
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
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Home", color = Color.Blue) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_HABITLIST) {
                            popUpTo(ROUTE_HABITLIST) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Habits", tint = Color.White) },
                    label = { Text("Habits", color = Color.Blue) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_MOTIVATION) {
                            popUpTo(ROUTE_HABITLIST) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Motivation", tint = Color.White) },
                    label = { Text("Motivations", color = Color.Blue) }
                )
            }
        },
        containerColor = Color.Blue
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .padding(padding)
                .padding(16.dp)
        ) {
            if (error != null) {
                Text(text = error!!, color = Color.Red)
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(habitList, key = { it.id }) { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(habit.text, color = Color.Blue, fontSize = 16.sp)
                            Row {
                                TextButton(onClick = {
                                    habitToEdit = habit
                                    updatedText = habit.text
                                }) {
                                    Text("Update", color = Color.Green)
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                TextButton(onClick = {
                                    viewModel.deleteHabit(
                                        habit.id,
                                        onSuccess = {
                                            viewModel.fetchHabits { error = it }
                                        },
                                        onError = { error = it }
                                    )
                                }) {
                                    Text("Delete", color = Color.Red)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(ROUTE_HABITS) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Add Habit")
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun HabitListScreenPreview() {
    HabitListScreen(
        rememberNavController())
}
