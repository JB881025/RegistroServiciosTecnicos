package com.example.registroserviciostecnicos.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.registroserviciostecnicos.R
import com.example.registroserviciostecnicos.utils.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class DetalleServicioActivity : AppCompatActivity() {

    private lateinit var tvCliente: TextView
    private lateinit var tvDireccion: TextView
    private lateinit var tvTipo: TextView
    private lateinit var tvDescripcion: TextView
    private lateinit var tvFecha: TextView
    private lateinit var tvEstado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_servicio)

        val servicioId = intent.getLongExtra("servicio_id", 0)

        tvCliente = findViewById(R.id.tvDetalleCliente)
        tvDireccion = findViewById(R.id.tvDetalleDireccion)
        tvTipo = findViewById(R.id.tvDetalleTipo)
        tvDescripcion = findViewById(R.id.tvDetalleDescripcion)
        tvFecha = findViewById(R.id.tvDetalleFecha)
        tvEstado = findViewById(R.id.tvDetalleEstado)

        if (servicioId > 0) {
            val dbHelper = DatabaseHelper(this)
            val servicio = dbHelper.obtenerServicioPorId(servicioId)
            servicio?.let {
                tvCliente.text = "Cliente: ${it.clienteNombre}"
                tvDireccion.text = "Dirección: ${it.direccion}"
                tvTipo.text = "Tipo: ${it.tipoServicio}"
                tvDescripcion.text = "Descripción: ${it.observaciones}"
                tvFecha.text = "Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(it.fechaServicio))}"
                tvEstado.text = "Estado: ${it.estado}"
            }
        }
    }
}