package com.example.abcd.data

import androidx.lifecycle.ViewModel
import com.example.abcd.models.Motivation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow

class Addmotivationsmodels : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val motivationsCollection = db.collection("motivations")

    private val _motivations = MutableStateFlow<List<Motivation>>(emptyList())

    init {
        FirebaseFirestore.setLoggingEnabled(true)
    }

    fun addMotivation(
        text: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val data = hashMapOf("text" to text)
        motivationsCollection
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error adding motivation") }
    }

    fun fetchMotivations(
        onFetch: (List<Motivation>) -> Unit,
        onError: (String) -> Unit,
    ) {
        motivationsCollection
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.map { doc ->
                    Motivation(id = doc.id, text = doc.getString("text") ?: "")
                }
                println("Fetched motivations: size=${list.size}, ids=${list.map { it.id }}")
                onFetch(list)
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error fetching motivations") }
    }

    fun deleteMotivation(
        id: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        motivationsCollection
            .document(id)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error deleting motivation") }
    }

    // New method for updating motivation text
    fun updateMotivation(
        id: String,
        newText: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        motivationsCollection
            .document(id)
            .update("text", newText)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onError(e.message ?: "Error updating motivation") }
    }
}
