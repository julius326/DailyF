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
import com.example.abcd.navigations.ROUTE_HABITLIST
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
    val isMoved: Boolean,
    val isDone: Boolean = false // NEW: for checkbox state (can sync with Firestore if needed)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayTaskScreen(navController: NavHostController) {
    val db = remember { FirebaseFirestore.getInstance() }
    var tasks by remember { mutableStateOf<List<TodayTask>>(emptyList()) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    // NEW: Checkbox states per task ID
    val checkedStates = remember { mutableStateMapOf<String, Boolean>() }

    val todayDate = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

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
                        val taskId = doc.id
                        // Keep previous checked state if exists
                        val isDone = checkedStates[taskId] ?: false
                        TodayTask(title, time, taskId, isMoved, isDone)
                    }
                    tasks = list

                    // Preserve checkbox states
                    list.forEach {
                        if (checkedStates[it.id] == null) {
                            checkedStates[it.id] = false
                        }
                    }
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
                title = { Text("Today's Tasks", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_HOME) }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF4B0082)
                )

            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF4B0082)) {
                NavigationBarItem(
                    selected = true,
                    onClick = { navController.navigate(ROUTE_HOME) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Blue) },
                    label = { Text("Home", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUTE_HABITLIST) },
                    icon = { Icon(Icons.Default.Refresh, contentDescription = "Habits", tint = Color.Blue) },
                    label = { Text("Habits", color = Color.White) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(ROUTE_TOMORROW) },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Motivations", tint = Color.Blue) },
                    label = { Text("Motivations", color = Color.White) }
                )
            }
        },
        containerColor = Color(0xFFADD8E6)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .background(Color(0xFFADD8E6))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("#", fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.weight(0.1f))
                Text("Task", fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.weight(0.5f))
                Text("Time", fontWeight = FontWeight.Bold, color = Color.Blue, modifier = Modifier.weight(0.2f))
                Spacer(modifier = Modifier.weight(0.2f)) // Space for checkbox/delete
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
                        Text("${index + 1}", color = Color.Blue, modifier = Modifier.weight(0.1f))

                        Column(modifier = Modifier.weight(0.5f)) {
                            Text(task.task, color = Color.Blue)
                            if (task.isMoved) {
                                Text(
                                    text = "Task moved",
                                    color = Color.Yellow,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Text(task.time, color = Color.Blue, modifier = Modifier.weight(0.2f))

                        Row(
                            modifier = Modifier.weight(0.2f),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = checkedStates[task.id] == true,
                                onCheckedChange = { isChecked ->
                                    checkedStates[task.id] = isChecked
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Color.Green,
                                    uncheckedColor = Color.Blue
                                )
                            )

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
                    navController.navigate(ROUTE_TOMORROW)
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
