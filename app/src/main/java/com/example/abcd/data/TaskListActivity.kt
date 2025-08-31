package com.example.abcd.data

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abcd.R
import com.example.abcd.TaskAdapter
import com.example.abcd.models.Task

class TaskListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private var tasks = mutableListOf<Task>()  // temp local list for now

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        recyclerView = findViewById(R.id.recycler_tasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TaskAdapter(tasks,
            onEditClick = { task ->
                // TODO: navigate to edit screen with task data
            },
            onDeleteClick = { task ->
                // TODO: delete task from database (or list for now)
                tasks.remove(task)
                adapter.updateTasks(tasks)
            }
        )

        recyclerView.adapter = adapter

        // Temporary fake data
        tasks.add(Task(id = "1", title = "Buy groceries", time = "14:00"))
        tasks.add(Task(id = "2", title = "Call mom", time = "16:30"))
        adapter.updateTasks(tasks)
    }
}
