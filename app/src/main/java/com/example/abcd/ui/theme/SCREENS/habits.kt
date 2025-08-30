package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
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
import com.example.abcd.navigations.ROUTE_HOME

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitScreen(navController: NavHostController, viewModel: HabitViewModel = viewModel()) {
    var habitText by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = @androidx.compose.runtime.Composable {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add Habits", color = Color.Blue, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    Row {
                        IconButton(onClick = {
                            navController.popBackStack() // Back action
                        }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Blue
                            )
                        }
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
                containerColor = Color.Blue
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
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
