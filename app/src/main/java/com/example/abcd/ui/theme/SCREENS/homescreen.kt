package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_MOTIVATION
import com.example.abcd.navigations.ROUTE_TODAY
import com.example.abcd.navigations.ROUTE_TOMORROW

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedCard by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = @androidx.compose.runtime.Composable {
            CenterAlignedTopAppBar(
                title = {
                    Text("Organize.Relax", color = Color.Blue, fontWeight = FontWeight.Bold)
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
                    onClick = { /* Handle home click */ },
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClickableCard(title = "Today's task", selected = selectedCard == "today") {
                selectedCard = "today"
                navController.navigate(ROUTE_TODAY) // Replace with your actual route
            }
            ClickableCard(title = "Tomorrow's task", selected = selectedCard == "tomorrow") {
                selectedCard = "tomorrow"
                navController.navigate(ROUTE_TOMORROW)
            }
            ClickableCard(title = "Habits", selected = selectedCard == "habits") {
                selectedCard = "habits"
                navController.navigate(ROUTE_HABITLIST)
            }
            ClickableCard(title = "Motivations", selected = selectedCard == "motivations") {
                selectedCard = "motivations"
                navController.navigate(ROUTE_MOTIVATION)
            }
        }
    }
}

@Composable
fun ClickableCard(title: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                color =  Color.Blue,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
