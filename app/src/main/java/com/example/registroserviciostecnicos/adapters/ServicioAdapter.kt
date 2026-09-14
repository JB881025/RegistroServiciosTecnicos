package com.example.registroserviciostecnicos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.registroserviciostecnicos.R
import com.example.registroserviciostecnicos.models.Servicio
import java.text.SimpleDateFormat
import java.util.*

class ServicioAdapter(
    private val servicios: List<Servicio>,
    private val onItemClick: (Servicio) -> Unit
) : RecyclerView.Adapter<ServicioAdapter.ServicioViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_servicio, parent, false)
        return ServicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicioViewHolder, position: Int) {
        val servicio = servicios[position]
        holder.bind(servicio)
        holder.itemView.setOnClickListener { onItemClick(servicio) }
    }

    override fun getItemCount(): Int = servicios.size

    class ServicioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCliente: TextView = itemView.findViewById(R.id.tvCliente)
        private val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
        private val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        private val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)

        fun bind(servicio: Servicio) {
            tvCliente.text = servicio.clienteNombre
            tvTipo.text = servicio.tipoServicio
            tvFecha.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(Date(servicio.fechaServicio))
            tvEstado.text = servicio.estado
            // Cambiar color del estado
            tvEstado.setTextColor(
                if (servicio.estado == "enviado")
                    android.graphics.Color.parseColor("#4CAF50") // Verde
                else
                    android.graphics.Color.parseColor("#FF9800") // Naranja
            )
        }
    }
}