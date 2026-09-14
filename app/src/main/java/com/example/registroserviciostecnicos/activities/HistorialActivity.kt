package com.example.registroserviciostecnicos.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.registroserviciostecnicos.R
import com.example.registroserviciostecnicos.adapters.ServicioAdapter
import com.example.registroserviciostecnicos.models.Servicio
import com.example.registroserviciostecnicos.utils.DatabaseHelper

class HistorialActivity : AppCompatActivity() {

    private lateinit var rvHistorial: RecyclerView
    private lateinit var dbHelper: DatabaseHelper
    private val serviciosList = mutableListOf<Servicio>()
    private lateinit var adapter: ServicioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_historial)

        dbHelper = DatabaseHelper(this)
        rvHistorial = findViewById(R.id.rvHistorial)

        setupRecyclerView()
        loadServicios()
    }

    private fun setupRecyclerView() {
        adapter = ServicioAdapter(serviciosList) { servicio ->
            // ✅ Abrir detalle al hacer clic
            val intent = Intent(this, DetalleServicioActivity::class.java)
            intent.putExtra("servicio_id", servicio.id)
            startActivity(intent)
        }
        rvHistorial.layoutManager = LinearLayoutManager(this)
        rvHistorial.adapter = adapter
    }

    private fun loadServicios() {
        serviciosList.clear()
        serviciosList.addAll(dbHelper.obtenerServicios())
        adapter.notifyDataSetChanged()
    }
}