package com.example.colegioapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.colegioapp.R
import com.example.colegioapp.utils.FirebaseUtils
import com.google.firebase.database.DatabaseReference

class RegisterActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etNombre: EditText
    private lateinit var btnRegister: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etEmail = findViewById(R.id.editRegisterEmail)
        etPassword = findViewById(R.id.editRegisterPassword)
        etNombre = findViewById(R.id.etNombre)
        btnRegister = findViewById(R.id.btnRegister)

        dbRef = FirebaseUtils.db.child("usuarios")

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val pass = etPassword.text.toString().trim()
            val nombre = etNombre.text.toString().trim()

            if (email.isEmpty() || pass.isEmpty() || nombre.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            FirebaseUtils.auth.createUserWithEmailAndPassword(email, pass)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: ""

                    val userMap = mapOf(
                        "uid" to uid,
                        "nombre" to nombre,
                        "email" to email
                    )

                    dbRef.child(uid).setValue(userMap)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener { e: Exception ->
                            Toast.makeText(this, "Error al guardar en DB: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .addOnFailureListener { e: Exception ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
