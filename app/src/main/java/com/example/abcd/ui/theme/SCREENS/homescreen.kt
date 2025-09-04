package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.abcd.data.AuthViewModel
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_LOGIN
import com.example.abcd.navigations.ROUTE_MOTIVATION
import com.example.abcd.navigations.ROUTE_TODAY
import com.example.abcd.navigations.ROUTE_TOMORROW


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel()
    var selectedCard by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Organize.Relax",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Row {
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = {
                            navController.navigate(ROUTE_HOME)
                        }) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                tint = Color.Blue
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        authViewModel.logoutUser {
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
                    containerColor = Color(0xFF4B0082)
                ) // Dark purple
                )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B0082)) {
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Already home */ },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.Blue
                        )
                    },
                    label = { Text("Home", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(ROUTE_HABITLIST) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Habits",
                            tint = Color.Blue
                        )
                    },
                    label = { Text("Habits", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate(ROUTE_MOTIVATION) {
                            popUpTo(ROUTE_HOME) { inclusive = true }
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Motivations",
                            tint = Color.Blue
                        )
                    },
                    label = { Text("Motivations", color = Color.White) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFADD8E6)) // Light Blue Background
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ClickableCard(
                title = "Today's task",
                selected = selectedCard == "today",
//                cardColor = Color.White// Soft Coral
            ) {
                selectedCard = "today"
                navController.navigate(ROUTE_TODAY)
            }

            ClickableCard(
                title = "Tomorrow's task",
                selected = selectedCard == "tomorrow",
//               cardColor = Color.White// Lavender
            ) {
                selectedCard = "tomorrow"
                navController.navigate(ROUTE_TOMORROW)
            }

            ClickableCard(
                title = "Habits",
                selected = selectedCard == "habits",
//                cardColor = Color.White // Mint Green
            ) {
                selectedCard = "habits"
                navController.navigate(ROUTE_HABITLIST)
            }

            ClickableCard(
                title = "Motivations",
                selected = selectedCard == "motivations",
//                cardColor = Color.White // Sky Blue
            ) {
                selectedCard = "motivations"
                navController.navigate(ROUTE_MOTIVATION)
            }
        }
    }
}

//@Composable
//fun ClickableCard(title: String, selected: Boolean, cardColor: Color, onClick: () -> Unit) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(120.dp)
//            .clickable { onClick() },
//        colors = CardDefaults.cardColors(
//            containerColor = cardColor
//        ),
//        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = title,
//                color = Color.Blue,
//                fontSize = 22.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}
@Composable
fun ClickableCard(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    // Icon mapping (optional - customize this)
    val icon = when (title) {
        "Today's task" -> Icons.Default.CheckCircle
        "Tomorrow's task" -> Icons.Default.Star
        "Habits" -> Icons.Default.Favorite
        "Motivations" -> Icons.Default.Star
        else -> Icons.Default.Info
    }

    // Gradient background based on title
    val gradientColors = when (title) {
        "Today's task" -> listOf(Color(0xFFE96443), Color(0xFF904E95)) // Coral to purple
        "Tomorrow's task" -> listOf(Color(0xFF2193b0), Color(0xFF6dd5ed)) // Blue gradient
        "Habits" -> listOf(Color(0xFF56ab2f), Color(0xFFa8e063)) // Green gradient
        "Motivations" -> listOf(Color(0xFFf12711), Color(0xFFf5af19)) // Orange gradient
        else -> listOf(Color.LightGray, Color.Gray)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Box(
            modifier = Modifier
                .background(Brush.linearGradient(gradientColors))
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp),
                   tint = Color.White
                )

                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
