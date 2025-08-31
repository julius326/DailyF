package com.example.abcd.ui.theme.SCREENS

import android.app.TimePickerDialog
import android.widget.TimePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.abcd.navigations.ROUTE_HOME
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.util.Calendar

// Reuse your TodayTask data class
data class TodayTaskItem(
    val task: String,
    val time: String,
    val id: String,
    val isMoved: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TomorrowTaskScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = remember { FirebaseFirestore.getInstance() }

    var taskText by remember { mutableStateOf("") }
    var taskAdded by remember { mutableStateOf(false) }

    // Time picker states for adding new task
    val calendar = Calendar.getInstance()
    var hour by remember { mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY)) }
    var minute by remember { mutableStateOf(calendar.get(Calendar.MINUTE)) }
    var showTimePicker by remember { mutableStateOf(false) }

    // Tasks list state
    var tasks by remember { mutableStateOf<List<TodayTaskItem>>(emptyList()) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    // Edit dialog states
    var editTask by remember { mutableStateOf<TodayTaskItem?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var editTaskText by remember { mutableStateOf("") }
    var editHour by remember { mutableStateOf(0) }
    var editMinute by remember { mutableStateOf(0) }

    // Calculate tomorrow midnight timestamp (same as when adding task)
    val calendarTomorrow = remember {
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }
    val tomorrowMidnight = calendarTomorrow.timeInMillis

    // Firestore listener to fetch tomorrow's tasks
    LaunchedEffect(Unit) {
        listenerRegistration = db.collection("tasks")
            .whereEqualTo("taskDate", tomorrowMidnight)
            .addSnapshotListener { snapshot, error ->
                if (error != null) return@addSnapshotListener
                if (snapshot != null) {
                    val list = snapshot.documents.mapNotNull { doc ->
                        val title = doc.getString("title") ?: return@mapNotNull null
                        val time = doc.getString("time") ?: "00:00"
                        val isMoved = doc.getBoolean("isMoved") ?: false
                        TodayTaskItem(title, time, doc.id, isMoved)
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

    // Show TimePickerDialog for adding new task
    if (showTimePicker) {
        TimePickerDialog(
            context,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                hour = selectedHour
                minute = selectedMinute
                showTimePicker = false
            },
            hour,
            minute,
            true
        ).show()
    }

    // Show Edit Task Dialog
    if (showEditDialog && editTask != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Task") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editTaskText,
                        onValueChange = { editTaskText = it },
                        label = { Text("Task") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        TimePickerDialog(
                            context,
                            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                                editHour = selectedHour
                                editMinute = selectedMinute
                            },
                            editHour,
                            editMinute,
                            true
                        ).show()
                    }) {
                        Text("Select Time: %02d:%02d".format(editHour, editMinute))
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    db.collection("tasks").document(editTask!!.id)
                        .update(
                            mapOf(
                                "title" to editTaskText,
                                "time" to String.format("%02d:%02d", editHour, editMinute)
                            )
                        )
                        .addOnSuccessListener {
                            showEditDialog = false
                        }
                        .addOnFailureListener {
                            // Handle error if needed
                        }
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tomorrow's Task", color = Color.Blue) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_HOME) }) {
                        Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.Blue)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Blue)
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = taskText,
                onValueChange = {
                    taskText = it
                    taskAdded = false
                },
                label = { Text(text = "Enter task for tomorrow") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedIndicatorColor = Color.White,
                    unfocusedIndicatorColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { showTimePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = String.format("Select Time: %02d:%02d", hour, minute), fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (taskText.isNotBlank()) {
                        val task = hashMapOf(
                            "title" to taskText,
                            "time" to String.format("%02d:%02d", hour, minute),
                            "isMoved" to false,
                            "createdAt" to System.currentTimeMillis(),
                            "taskDate" to tomorrowMidnight
                        )

                        db.collection("tasks")
                            .add(task)
                            .addOnSuccessListener {
                                taskAdded = true
                                taskText = ""
                            }
                            .addOnFailureListener {
                                // Handle failure if needed
                            }
                    }
                },
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth()
            ) {
                Text("Add for Tomorrow", fontSize = 18.sp)
            }

            if (taskAdded) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Task added!", color = Color.White)
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Display list of tasks for tomorrow
            if (tasks.isNotEmpty()) {
                Text("Tomorrow's Tasks:", color = Color.White, fontSize = 20.sp, modifier = Modifier.padding(bottom = 10.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(tasks) { task ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(task.task, color = Color.White, fontSize = 16.sp)
                                Text("At ${task.time}", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                            }
                            Row {
                                Button(onClick = {
                                    editTask = task
                                    editTaskText = task.task
                                    val parts = task.time.split(":")
                                    editHour = parts.getOrNull(0)?.toIntOrNull() ?: 0
                                    editMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0
                                    showEditDialog = true
                                }) {
                                    Text("Edit")
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(onClick = {
                                    db.collection("tasks").document(task.id).delete()
                                }) {
                                    Text("Delete")
                                }
                            }
                        }
                        Divider(color = Color.White.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TomorrowTaskScreenPreview() {
    TomorrowTaskScreen(navController = rememberNavController())
}
