package com.example.colegioapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.colegioapp.R
import com.example.colegioapp.model.Estudiante
import com.example.colegioapp.utils.FirebaseUtils
import com.google.firebase.database.DatabaseReference

class EstudianteActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEdad: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnGuardar: Button
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudiante)

        etNombre = findViewById(R.id.etNombre)
        etEdad = findViewById(R.id.etEdad)
        etDireccion = findViewById(R.id.etDireccion)
        etTelefono = findViewById(R.id.etTelefono)
        btnGuardar = findViewById(R.id.btnGuardarEstudiante)

        dbRef = FirebaseUtils.db.child("estudiantes")

        btnGuardar.setOnClickListener {
            guardarEstudiante()
        }
    }

    private fun guardarEstudiante() {
        val nombre = etNombre.text.toString().trim()
        val edadStr = etEdad.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        if (nombre.isEmpty() || edadStr.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = edadStr.toIntOrNull()
        if (edad == null || edad <= 0) {
            Toast.makeText(this, "Edad inválida", Toast.LENGTH_SHORT).show()
            return
        }

        val id = dbRef.push().key ?: return
        val estudiante = Estudiante(
            id = id,
            nombre = nombre,
            apellido = "",
            grado = "",
            edad = edad,
            direccion = direccion,
            telefono = telefono
        )

        dbRef.child(id).setValue(estudiante)
            .addOnSuccessListener {
                Toast.makeText(this, "Estudiante guardado", Toast.LENGTH_SHORT).show()
                limpiarCampos()
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun limpiarCampos() {
        etNombre.text?.clear()
        etEdad.text?.clear()
        etDireccion.text?.clear()
        etTelefono.text?.clear()
    }
}
