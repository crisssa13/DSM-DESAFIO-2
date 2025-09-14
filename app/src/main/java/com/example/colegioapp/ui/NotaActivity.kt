package com.example.colegioapp.ui

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.colegioapp.R
import com.example.colegioapp.model.Estudiante
import com.example.colegioapp.model.Nota
import com.example.colegioapp.utils.FirebaseUtils
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class NotaActivity : AppCompatActivity() {

    private lateinit var spEstudiantes: Spinner
    private lateinit var etGrado: EditText
    private lateinit var etMateria: EditText
    private lateinit var etNota: EditText
    private lateinit var btnGuardarNota: Button

    private val estudiantes = mutableListOf<Estudiante>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nota)

        spEstudiantes = findViewById(R.id.spEstudiantes)
        etGrado = findViewById(R.id.etGrado)
        etMateria = findViewById(R.id.etMateria)
        etNota = findViewById(R.id.etNota)
        btnGuardarNota = findViewById(R.id.btnGuardarNota)

        FirebaseUtils.db.child("estudiantes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                estudiantes.clear()
                for (child in snapshot.children) {
                    val est = child.getValue(Estudiante::class.java)
                    est?.let { estudiantes.add(it) }
                }

                val adapter = ArrayAdapter(
                    this@NotaActivity,
                    android.R.layout.simple_spinner_item,
                    estudiantes.map { it.nombre ?: "" }
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spEstudiantes.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@NotaActivity, "Error al cargar estudiantes", Toast.LENGTH_SHORT).show()
            }
        })

        btnGuardarNota.setOnClickListener {
            val grado = etGrado.text.toString().trim()
            val materia = etMateria.text.toString().trim()
            val notaFinalStr = etNota.text.toString().trim()

            if (grado.isEmpty() || materia.isEmpty() || notaFinalStr.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val notaFinal = notaFinalStr.toDoubleOrNull()
            if (notaFinal == null || notaFinal < 0 || notaFinal > 10) {
                Toast.makeText(this, "Nota inválida (0-10)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val estudiante = estudiantes.getOrNull(spEstudiantes.selectedItemPosition)
            if (estudiante == null || estudiante.id.isNullOrEmpty()) {
                Toast.makeText(this, "Estudiante inválido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = FirebaseUtils.db.child("notas").push().key ?: return@setOnClickListener
            val nota = Nota(id, estudiante.id, grado, materia, notaFinal)

            FirebaseUtils.db.child("notas").child(id).setValue(nota)
                .addOnSuccessListener {
                    Toast.makeText(this, "Nota registrada", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e: Exception ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
