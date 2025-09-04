package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.abcd.data.AuthViewModel
import com.example.abcd.data.HabitViewModel
import com.example.abcd.models.Habit
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_HABITS
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_LOGIN
import com.example.abcd.navigations.ROUTE_MOTIVATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitListScreen(
    navController: NavHostController,
    viewModel: HabitViewModel = viewModel(),
    AuthViewModel: AuthViewModel = viewModel()
) {
    val habitList by viewModel.habits.collectAsState()
    var error by remember { mutableStateOf<String?>(null) }

    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var updatedText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchHabits {
            error = it
        }
    }

    if (habitToEdit != null) {
        AlertDialog(
            onDismissRequest = { habitToEdit = null },
            title = { Text("Update Habit", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
            text = {
                TextField(
                    value = updatedText,
                    onValueChange = { updatedText = it },
                    label = { Text("Habit") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFF0F8FF),
                        unfocusedContainerColor = Color(0xFFF0F8FF),
                        focusedIndicatorColor = Color(0xFF4B0082),
                        unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f),
                        cursorColor = Color(0xFF4B0082)
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
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
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B0082)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { habitToEdit = null },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "My Habits",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_HOME)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        AuthViewModel.logoutUser {
                            navController.navigate(ROUTE_LOGIN) {
                                popUpTo(0)
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Logout",
                            tint = Color.Red
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF4B0082),
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B0082)) {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Home", color = Color.White) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_HABITLIST) {
                            popUpTo(ROUTE_HABITLIST) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Habits", tint = Color.White) },
                    label = { Text("Habits", color = Color.White) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_MOTIVATION) {
                            popUpTo(ROUTE_HABITLIST) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Motivation", tint = Color.White) },
                    label = { Text("Motivations", color = Color.White) }
                )
            }
        },
        containerColor = Color(0xFFADD8E6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFADD8E6))
                .padding(padding)
                .padding(16.dp)
        ) {
            if (error != null) {
                Text(text = error!!, color = Color.Red, fontWeight = FontWeight.SemiBold)
            }

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(habitList, key = { it.id }) { habit ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shadow(4.dp, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                habit.text,
                                color = Color(0xFF333333),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Row {
                                TextButton(onClick = {
                                    habitToEdit = habit
                                    updatedText = habit.text
                                }) {
                                    Text("Update", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
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
                                    Text("Delete", color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
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
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B0082))
            ) {
                Text("➕ Add New Habit", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
