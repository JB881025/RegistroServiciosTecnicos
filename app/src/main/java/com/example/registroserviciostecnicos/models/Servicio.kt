package com.example.registroserviciostecnicos.models

data class Servicio(
    val id: Long = 0,
    val clienteId: Long = 0,
    val clienteNombre: String = "",
    val direccion: String = "",
    val tipoServicio: String = "",
    val materiales: String = "",
    val observaciones: String = "",
    val fotos: List<String> = emptyList(),
    val fechaServicio: Long = System.currentTimeMillis(),
    val tecnicoId: String = "",
    val pdfUrl: String = "",
    val estado: String = "pendiente"
)