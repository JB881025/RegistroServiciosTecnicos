package com.example.registroserviciostecnicos.models

import java.io.Serializable

data class Cliente(
    val id: Long = 0,
    val nombre: String = "",
    val email: String = "",
    val estado: String = "activo"
) : Serializable