package com.example.abcd.data

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.abcd.models.Habit

class HabitViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val habitsCollection = db.collection("habits")

    private val _habits = MutableStateFlow<List<Habit>>(emptyList())
    val habits: StateFlow<List<Habit>> = _habits

    fun fetchHabits(onError: (String) -> Unit) {
        habitsCollection.get()
            .addOnSuccessListener { snapshot ->
                val habitList = snapshot.map { doc ->
                    Habit(id = doc.id, text = doc.getString("text") ?: "")
                }
                _habits.value = habitList
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Error fetching habits")
            }
    }

    fun addHabit(
        text: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val data = hashMapOf("text" to text)
        habitsCollection
            .add(data)
            .addOnSuccessListener {
                onSuccess()
                fetchHabits {} // Refresh the list
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error adding habit") }
    }

    fun deleteHabit(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        habitsCollection
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Failed to delete habit") }
    }

    // New method to update a habit's text
    fun updateHabit(
        id: String,
        newText: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        habitsCollection
            .document(id)
            .update("text", newText)
            .addOnSuccessListener {
                onSuccess()
                fetchHabits {} // Refresh the list after update
            }
            .addOnFailureListener { e ->
                onError(e.message ?: "Failed to update habit")
            }
    }
}
