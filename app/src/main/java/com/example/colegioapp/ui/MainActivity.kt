package com.example.colegioapp.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.colegioapp.R
import com.example.colegioapp.utils.FirebaseUtils

class MainActivity : AppCompatActivity() {

    private lateinit var btnEstudiante: Button
    private lateinit var btnNota: Button
    private lateinit var btnListar: Button
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnEstudiante = findViewById(R.id.btnEstudiante)
        btnNota = findViewById(R.id.btnNota)
        btnListar = findViewById(R.id.btnListar)
        btnLogout = findViewById(R.id.btnLogout)

        btnEstudiante.setOnClickListener {
            startActivity(Intent(this, EstudianteActivity::class.java))
        }

        btnNota.setOnClickListener {
            startActivity(Intent(this, NotaActivity::class.java))
        }

        btnListar.setOnClickListener {
            startActivity(Intent(this, ListarActivity::class.java))
        }

        btnLogout.setOnClickListener {
            FirebaseUtils.auth.signOut()
            finish()
        }
    }
}
