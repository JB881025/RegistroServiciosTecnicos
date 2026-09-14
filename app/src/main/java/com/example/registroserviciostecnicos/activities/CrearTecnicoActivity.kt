package com.example.registroserviciostecnicos.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.registroserviciostecnicos.R
import com.example.registroserviciostecnicos.utils.DatabaseHelper

class CrearTecnicoActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnGuardar: Button
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_tecnico)

        dbHelper = DatabaseHelper(this)

        etNombre = findViewById(R.id.etTecnicoNombre)
        etEmail = findViewById(R.id.etTecnicoEmail)
        etPassword = findViewById(R.id.etTecnicoPassword)
        btnGuardar = findViewById(R.id.btnGuardarTecnico)

        btnGuardar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val id = dbHelper.agregarTecnico(nombre, email, password)
                if (id > 0) {
                    Toast.makeText(this, "Técnico $nombre creado correctamente", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Error al crear técnico", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}