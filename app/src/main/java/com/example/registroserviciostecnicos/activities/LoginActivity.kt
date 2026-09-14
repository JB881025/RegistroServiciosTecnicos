package com.example.registroserviciostecnicos.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.registroserviciostecnicos.R
import android.util.Patterns

class LoginActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvError: TextView

    // ✅ Credenciales
    private val ADMIN_EMAIL = "proyectos@jbingenieria.com.co"
    private val ADMIN_PASSWORD = "12345"

    // ✅ Técnico de prueba
    private val TECNICO_EMAIL = "tecnico@jbingenieria.com.co"
    private val TECNICO_PASSWORD = "12345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvError = findViewById(R.id.tvError)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                mostrarError("Completa todos los campos")
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                mostrarError("Ingresa un correo válido")
                return@setOnClickListener
            }

            // ✅ Verificar si es Administrador
            if (email == ADMIN_EMAIL && password == ADMIN_PASSWORD) {
                Toast.makeText(this, "Bienvenido Administrador", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("perfil", "Administrador")
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            // ✅ Verificar si es Técnico
            if (email == TECNICO_EMAIL && password == TECNICO_PASSWORD) {
                Toast.makeText(this, "Bienvenido Técnico", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("perfil", "Tecnico")
                startActivity(intent)
                finish()
                return@setOnClickListener
            }

            mostrarError("Credenciales incorrectas")
        }
    }

    private fun mostrarError(mensaje: String) {
        tvError.text = mensaje
        tvError.visibility = View.VISIBLE
        Handler(Looper.getMainLooper()).postDelayed({
            tvError.visibility = View.GONE
        }, 3000)
    }
}