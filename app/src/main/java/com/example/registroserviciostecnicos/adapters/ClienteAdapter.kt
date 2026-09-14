package com.example.registroserviciostecnicos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.registroserviciostecnicos.R
import com.example.registroserviciostecnicos.models.Cliente

class ClienteAdapter(
    private val clientes: List<Cliente>,
    private val onItemClick: (Cliente) -> Unit
) : RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val cliente = clientes[position]
        holder.bind(cliente)
        holder.itemView.setOnClickListener { onItemClick(cliente) }
    }

    override fun getItemCount(): Int = clientes.size

    class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        private val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)

        fun bind(cliente: Cliente) {
            tvNombre.text = cliente.nombre
            tvEmail.text = if (cliente.email.isNotEmpty()) cliente.email else "Sin correo"
        }
    }
}