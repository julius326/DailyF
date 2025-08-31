package com.example.abcd.data

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import com.example.abcd.R
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class AddTaskActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etNote: EditText
    private lateinit var timePicker: TimePicker
    private lateinit var btnSave: Button

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        etTitle = findViewById(R.id.et_title)
        etNote = findViewById(R.id.et_note)
        timePicker = findViewById(R.id.time_picker)
        btnSave = findViewById(R.id.btn_save)

        // Save button click
        btnSave.setOnClickListener {
            saveTask()
        }
    }

    private fun saveTask() {
        val title = etTitle.text.toString().trim()
        val note = etNote.text.toString().trim()
        val hour = timePicker.hour
        val minute = timePicker.minute

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
            return
        }

        val timeFormatted = String.format("%02d:%02d", hour, minute)

        val task = hashMapOf(
            "title" to title,
            "note" to note,
            "time" to timeFormatted,
            "isMoved" to false
        )

        db.collection("tasks")
            .add(task)
            .addOnSuccessListener {
                Toast.makeText(this, "Task saved", Toast.LENGTH_SHORT).show()
                finish() // Close activity after saving
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
