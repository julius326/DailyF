package com.example.abcd.ui.theme.SCREENS

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.abcd.R
import com.example.abcd.data.Addmotivationsmodels
import com.example.abcd.models.Motivation
import com.example.abcd.navigations.ROUTE_ADDMOTIVATIONS
import com.example.abcd.navigations.ROUTE_HOME
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.TopAppBar
import com.example.abcd.navigations.ROUTE_HABITLIST
import com.example.abcd.navigations.ROUTE_MOTIVATION


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MotivationScreen(
    navController: NavHostController,
    viewModel: Addmotivationsmodels = viewModel(),
) {
    val motivations = remember { mutableStateListOf<Motivation>() }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(true) {
        isLoading = true
        viewModel.fetchMotivations(
            onFetch = {
                motivations.clear()
                motivations.addAll(it)
                isLoading = false
                error = null
            },
            onError = {
                error = it
                isLoading = false
            }
        )
    }

    Scaffold(
        topBar = @androidx.compose.runtime.Composable {
            CenterAlignedTopAppBar(
                title = {
                    Text("Motivations", color = Color.Blue, fontWeight = FontWeight.Bold)
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
                            popUpTo(ROUTE_MOTIVATION) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Home", tint = Color.White) },
                    label = { Text("Habits", color = Color.Blue) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate(ROUTE_MOTIVATION) {
                            popUpTo(ROUTE_MOTIVATION) { inclusive = true }
                        }
                    },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Home", tint = Color.White) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.m),
                contentDescription = "Motivation Image"
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> CircularProgressIndicator(color = Color.White)
                error != null -> Text(text = error!!, color = Color.Red)
                motivations.isEmpty() -> Text("No motivations found", color = Color.White)
                else -> {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(motivations, key = { it.id }) { motivation ->
                            MotivationItem(
                                motivation = motivation,
                                viewModel = viewModel,
                                onDelete = { motivations.remove(motivation) },
                                onUpdate = { updatedText ->
                                    // Update the local list with the new text
                                    val index = motivations.indexOfFirst { it.id == motivation.id }
                                    if (index != -1) {
                                        motivations[index] = motivation.copy(text = updatedText)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.navigate(ROUTE_ADDMOTIVATIONS) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Add Motivation")
            }
        }
    }
}


@Composable
fun MotivationItem(
    motivation: Motivation,
    viewModel: Addmotivationsmodels,
    onDelete: () -> Unit,
    onUpdate: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var newText by remember { mutableStateOf(motivation.text) }
    var error by remember { mutableStateOf<String?>(null) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Update Motivation") },
            text = {
                Column {
                    TextField(
                        value = newText,
                        onValueChange = { newText = it },
                        label = { Text("Motivation") },
                        singleLine = true,
                        isError = error != null
                    )
                    if (error != null) {
                        Text(error!!, color = Color.Red)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (newText.isBlank()) {
                        error = "Motivation cannot be empty"
                        return@Button
                    }
                    viewModel.updateMotivation(
                        id = motivation.id,
                        newText = newText,
                        onSuccess = {
                            showDialog = false
                            error = null
                            onUpdate(newText) // ✅ Update the list in parent
                        },
                        onError = { error = it }
                    )
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

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
            Text(
                text = motivation.text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Blue,
                modifier = Modifier.weight(1f)
            )
            Row {
                TextButton(onClick = { showDialog = true }) {
                    Text("Update", color = Color.Green)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(onClick = {
                    viewModel.deleteMotivation(
                        motivation.id,
                        onSuccess = { onDelete() },
                        onError = { /* optionally handle error */ }
                    )
                }) {
                    Text("Delete", color = Color.Red)
                }
            }
        }
    }
}



@Preview
@Composable
fun MotivationScreenpreview() {
    MotivationScreen(rememberNavController())
}
