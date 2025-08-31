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
import com.example.abcd.navigations.ROUTE_HOME
import com.example.abcd.navigations.ROUTE_TOMORROW
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.Calendar
import com.example.abcd.ui.theme.SCREENS.TodayTask


data class TodayTask(
    val task: String,
    val time: String,
    val id: String,
    val isMoved: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayTaskScreen(navController: NavHostController) {
    val db = remember { FirebaseFirestore.getInstance() }
    var tasks by remember { mutableStateOf<List<TodayTask>>(emptyList()) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    // Calculate today's midnight timestamp
    val todayDate = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    // Listen to Firestore for tasks with taskDate == todayDate
    LaunchedEffect(Unit) {
        listenerRegistration = db.collection("tasks")
            .whereEqualTo("taskDate", todayDate)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        val title = doc.getString("title") ?: return@mapNotNull null
                        val time = doc.getString("time") ?: "00:00"
                        val isMoved = doc.getBoolean("isMoved") ?: false
                        TodayTask(title, time, doc.id, isMoved)
                    }
                    tasks = list
                }
            }
    }

    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Today's Tasks", color = Color.Blue) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_HOME) }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Blue)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate("home") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Blue) },
                    label = { Text("Home", color = Color.Blue) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("habits") },
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Habits", tint = Color.Blue) },
                    label = { Text("Habits", color = Color.Blue) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("motivations") },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Motivations", tint = Color.Blue) },
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("#", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(0.1f))
                Text("Task", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(0.5f))
                Text("Time", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(0.2f))
                Text("Actions", fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.weight(0.2f))
            }

            Divider(color = Color.White, thickness = 1.dp)

            LazyColumn {
                itemsIndexed(tasks) { index, task ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${index + 1}", color = Color.White, modifier = Modifier.weight(0.1f))

                        Column(modifier = Modifier.weight(0.5f)) {
                            Text(task.task, color = Color.White)
                            if (task.isMoved) {
                                Text(
                                    text = "Task moved",
                                    color = Color.Yellow,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(task.time, color = Color.White, modifier = Modifier.weight(0.2f))

                        Row(
                            modifier = Modifier.weight(0.2f),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = {
                                // TODO: Add edit navigation here
                            }) {
                                Text("Edit", color = Color.Cyan, fontSize = 12.sp)
                            }
                            TextButton(onClick = {
                                db.collection("tasks").document(task.id).delete()
                            }) {
                                Text("Delete", color = Color.Red, fontSize = 12.sp)
                            }
                        }
                    }
                    Divider(color = Color.White.copy(alpha = 0.3f))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Navigate to TomorrowTaskScreen or AddTaskActivity
                    navController.navigate(ROUTE_TOMORROW )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add Task for Tomorrow", fontSize = 18.sp)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTodayTaskScreenSimple() {
    TodayTaskScreen(navController = rememberNavController())
}

