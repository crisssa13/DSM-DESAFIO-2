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

class EditarEstudianteActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private var estudianteId: String? = null

    private lateinit var etNombre: EditText
    private lateinit var etEdad: EditText
    private lateinit var etDireccion: EditText
    private lateinit var etTelefono: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_estudiante)

        dbRef = FirebaseUtils.db.child("estudiantes")

        etNombre = findViewById(R.id.etNombre)
        etEdad = findViewById(R.id.etEdad)
        etDireccion = findViewById(R.id.etDireccion)
        etTelefono = findViewById(R.id.etTelefono)
        btnActualizar = findViewById(R.id.btnActualizarEstudiante)
        btnEliminar = findViewById(R.id.btnEliminarEstudiante)

        estudianteId = intent.getStringExtra("id")
        val nombre = intent.getStringExtra("nombre")
        val edad = intent.getIntExtra("edad", 0)
        val direccion = intent.getStringExtra("direccion")
        val telefono = intent.getStringExtra("telefono")

        etNombre.setText(nombre)
        etEdad.setText(edad.toString())
        etDireccion.setText(direccion)
        etTelefono.setText(telefono)

        btnActualizar.setOnClickListener {
            actualizarEstudiante()
        }

        btnEliminar.setOnClickListener {
            eliminarEstudiante()
        }
    }

    private fun actualizarEstudiante() {
        val id = estudianteId ?: return
        val nombre = etNombre.text.toString().trim()
        val edadStr = etEdad.text.toString().trim()
        val direccion = etDireccion.text.toString().trim()
        val telefono = etTelefono.text.toString().trim()

        if (nombre.isEmpty() || edadStr.isEmpty() || direccion.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val edad = edadStr.toIntOrNull() ?: 0

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
                Toast.makeText(this, "Estudiante actualizado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarEstudiante() {
        val id = estudianteId ?: return
        dbRef.child(id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Estudiante eliminado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
