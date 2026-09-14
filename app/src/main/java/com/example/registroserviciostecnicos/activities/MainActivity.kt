package com.example.registroserviciostecnicos.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.registroserviciostecnicos.R

class MainActivity : AppCompatActivity() {

    private lateinit var btnClientes: Button
    private lateinit var btnNuevoServicio: Button
    private lateinit var btnHistorial: Button
    private lateinit var btnLogout: Button
    private lateinit var btnCrearTecnico: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val perfil = intent.getStringExtra("perfil") ?: "Tecnico"

        btnClientes = findViewById(R.id.btnClientes)
        btnNuevoServicio = findViewById(R.id.btnNuevoServicio)
        btnHistorial = findViewById(R.id.btnHistorial)
        btnLogout = findViewById(R.id.btnLogout)
        btnCrearTecnico = findViewById(R.id.btnCrearTecnico)

        // ✅ Mostrar opciones según perfil
        btnClientes.visibility = if (perfil == "Administrador") View.VISIBLE else View.GONE
        btnHistorial.visibility = if (perfil == "Administrador") View.VISIBLE else View.GONE
        btnCrearTecnico.visibility = if (perfil == "Administrador") View.VISIBLE else View.GONE

        btnClientes.setOnClickListener {
            startActivity(Intent(this, ClientesActivity::class.java))
        }

        btnNuevoServicio.setOnClickListener {
            startActivity(Intent(this, NuevoServicioActivity::class.java))
        }

        btnHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        btnCrearTecnico.setOnClickListener {
            startActivity(Intent(this, CrearTecnicoActivity::class.java))
        }

        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}