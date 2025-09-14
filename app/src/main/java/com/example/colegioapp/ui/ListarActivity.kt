package com.example.colegioapp.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.colegioapp.R
import com.example.colegioapp.model.Estudiante
import com.example.colegioapp.model.Nota
import com.example.colegioapp.utils.FirebaseUtils
import com.google.firebase.database.*

class ListarActivity : AppCompatActivity() {

    private lateinit var listViewEstudiantes: ListView
    private lateinit var listViewNotas: ListView

    private val estudiantesList = mutableListOf<Estudiante>()
    private val notasList = mutableListOf<Nota>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar)

        listViewEstudiantes = findViewById(R.id.listViewEstudiantes)
        listViewNotas = findViewById(R.id.listViewNotas)

        cargarEstudiantes()
        cargarNotas()
    }

    private fun cargarEstudiantes() {
        FirebaseUtils.db.child("estudiantes").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                estudiantesList.clear()
                for (est in snapshot.children) {
                    val estudiante = est.getValue(Estudiante::class.java)
                    if (estudiante != null) {
                        estudiantesList.add(estudiante)
                    }
                }

                val adapter = ArrayAdapter(
                    this@ListarActivity,
                    android.R.layout.simple_list_item_1,
                    estudiantesList
                )
                listViewEstudiantes.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListarActivity, "Error al cargar estudiantes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarNotas() {
        FirebaseUtils.db.child("notas").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notasList.clear()
                for (notaSnap in snapshot.children) {
                    val nota = notaSnap.getValue(Nota::class.java)
                    if (nota != null) {
                        notasList.add(nota)
                    }
                }

                val adapter = ArrayAdapter(
                    this@ListarActivity,
                    android.R.layout.simple_list_item_1,
                    notasList
                )
                listViewNotas.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ListarActivity, "Error al cargar notas", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
