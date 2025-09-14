package com.example.colegioapp.ui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.colegioapp.R
import com.example.colegioapp.model.Nota
import com.example.colegioapp.utils.FirebaseUtils
import com.google.firebase.database.DatabaseReference



class EditarNotaActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private var notaId: String? = null

    private lateinit var etEstudiante: EditText
    private lateinit var etGrado: EditText
    private lateinit var etMateria: EditText
    private lateinit var etNotaFinal: EditText
    private lateinit var btnActualizar: Button
    private lateinit var btnEliminar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_nota)

        dbRef = FirebaseUtils.db.child("notas")

        etEstudiante = findViewById(R.id.etEstudiante)
        etGrado = findViewById(R.id.etGrado)
        etMateria = findViewById(R.id.etMateria)
        etNotaFinal = findViewById(R.id.etNotaFinal)
        btnActualizar = findViewById(R.id.btnActualizarNota)
        btnEliminar = findViewById(R.id.btnEliminarNota)

        notaId = intent.getStringExtra("id")
        val estudiante = intent.getStringExtra("estudiante")
        val grado = intent.getStringExtra("grado")
        val materia = intent.getStringExtra("materia")
        val notaFinal = intent.getDoubleExtra("notaFinal", 0.0)

        etEstudiante.setText(estudiante)
        etGrado.setText(grado)
        etMateria.setText(materia)
        etNotaFinal.setText(notaFinal.toString())

        btnActualizar.setOnClickListener {
            actualizarNota()
        }

        btnEliminar.setOnClickListener {
            eliminarNota()
        }
    }

    private fun actualizarNota() {
        val id = notaId ?: return
        val estudiante = etEstudiante.text.toString().trim()
        val grado = etGrado.text.toString().trim()
        val materia = etMateria.text.toString().trim()
        val notaStr = etNotaFinal.text.toString().trim()

        if (estudiante.isEmpty() || grado.isEmpty() || materia.isEmpty() || notaStr.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val notaFinal = notaStr.toDoubleOrNull()
        if (notaFinal == null || notaFinal < 0 || notaFinal > 10) {
            Toast.makeText(this, "La nota debe estar entre 0 y 10", Toast.LENGTH_SHORT).show()
            return
        }

        val nota = Nota(id, estudiante, grado, materia, notaFinal)

        dbRef.child(id).setValue(nota)
            .addOnSuccessListener {
                Toast.makeText(this, "Nota actualizada", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarNota() {
        val id = notaId ?: return
        dbRef.child(id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e: Exception ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
