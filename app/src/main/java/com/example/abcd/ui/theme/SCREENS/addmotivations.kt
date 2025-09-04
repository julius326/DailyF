package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.abcd.data.Addmotivationsmodels
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_MOTIVATION


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMotivationScreen(navController: NavHostController) {
    val viewModel: Addmotivationsmodels = viewModel() // ✅ ViewModel must match renamed class

    var motivationText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = @androidx.compose.runtime.Composable {
            CenterAlignedTopAppBar(
                title = {
                    Text("Add Motivations", color = Color.White, fontWeight = FontWeight.Bold)
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
                    containerColor = Color(0xFF4B0082)
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor =  Color(0xFF4B0082)) {
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
                            popUpTo(ROUTE_MOTIVATION) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Habits", color = Color.White) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_MOTIVATION) {
                            popUpTo(ROUTE_MOTIVATION) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Motivations", color = Color.White) }
                )
            }
        },


    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFADD8E6))
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = motivationText,
                onValueChange = { motivationText = it },
                label = { Text("Write your motivation") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Blue,
                    unfocusedTextColor = Color.Black,
                    focusedLabelColor = Color.Cyan,
                    unfocusedLabelColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(onClick = {
                if (motivationText.isNotBlank()) {
                    viewModel.addMotivation(
                        text = motivationText,
                        onSuccess = {
                            motivationText = ""
                            navController.popBackStack()
                        },
                        onError = { errorMessage = it }
                    )
                } else {
                    errorMessage = "Motivation can't be empty"
                }
            }) {
                Text("Save Motivation", fontSize = 18.sp)
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }
        }
    }
}



@Preview
@Composable

fun AddMotivationPreview(){
    AddMotivationScreen(rememberNavController())
}
