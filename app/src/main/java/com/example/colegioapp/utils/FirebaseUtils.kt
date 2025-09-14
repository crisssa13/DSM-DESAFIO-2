package com.example.colegioapp.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtils {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://colegioapp-ddfd1-default-rtdb.firebaseio.com/")

    val db: DatabaseReference = database.reference

    fun currentUserUid(): String? {
        return auth.currentUser?.uid
    }
}
