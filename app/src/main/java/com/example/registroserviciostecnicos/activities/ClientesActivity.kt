package com.example.registroserviciostecnicos.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.registroserviciostecnicos.R
import com.example.registroserviciostecnicos.adapters.ClienteAdapter
import com.example.registroserviciostecnicos.models.Cliente
import com.example.registroserviciostecnicos.utils.DatabaseHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ClientesActivity : AppCompatActivity() {

    private lateinit var rvClientes: RecyclerView
    private lateinit var fabAddCliente: FloatingActionButton
    private lateinit var adapter: ClienteAdapter
    private lateinit var dbHelper: DatabaseHelper
    private val clientesList = mutableListOf<Cliente>()
    private var modoSeleccion = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        dbHelper = DatabaseHelper(this)
        modoSeleccion = intent.getBooleanExtra("seleccionar", false)

        rvClientes = findViewById(R.id.rvClientes)
        fabAddCliente = findViewById(R.id.fabAddCliente)

        setupRecyclerView()
        loadClientes()

        fabAddCliente.setOnClickListener {
            mostrarDialogoAgregarCliente()
        }
    }

    private fun setupRecyclerView() {
        adapter = ClienteAdapter(clientesList) { cliente ->
            if (modoSeleccion) {
                val resultIntent = Intent()
                resultIntent.putExtra("cliente_id", cliente.id)
                resultIntent.putExtra("cliente_nombre", cliente.nombre)
                resultIntent.putExtra("cliente_email", cliente.email)
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Cliente: ${cliente.nombre}", Toast.LENGTH_SHORT).show()
            }
        }
        rvClientes.layoutManager = LinearLayoutManager(this)
        rvClientes.adapter = adapter
    }

    private fun loadClientes() {
        clientesList.clear()
        clientesList.addAll(dbHelper.obtenerClientesActivos())
        adapter.notifyDataSetChanged()
    }

    private fun mostrarDialogoAgregarCliente() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_cliente, null)
        builder.setView(view)

        builder.setPositiveButton("Guardar") { _, _ ->
            val nombre = view.findViewById<EditText>(R.id.etNombre).text.toString().trim()
            val email = view.findViewById<EditText>(R.id.etEmail).text.toString().trim()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "El nombre es obligatorio", Toast.LENGTH_SHORT).show()
                return@setPositiveButton
            }

            val cliente = Cliente(
                nombre = nombre,
                email = email
            )

            val id = dbHelper.agregarCliente(cliente)
            if (id > 0) {
                Toast.makeText(this, "Cliente agregado", Toast.LENGTH_SHORT).show()
                loadClientes()
            } else {
                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
}