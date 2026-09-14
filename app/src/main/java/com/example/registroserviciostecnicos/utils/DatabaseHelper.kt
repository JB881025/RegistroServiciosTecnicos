package com.example.registroserviciostecnicos.utils

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.registroserviciostecnicos.models.Cliente
import com.example.registroserviciostecnicos.models.Servicio

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "registro.db"
        private const val DATABASE_VERSION = 2  // ✅ Actualizado para nueva tabla

        // Tabla Clientes
        private const val TABLE_CLIENTES = "clientes"
        private const val COL_ID = "id"
        private const val COL_NOMBRE = "nombre"
        private const val COL_EMAIL = "email"
        private const val COL_ESTADO = "estado"

        // Tabla Servicios
        private const val TABLE_SERVICIOS = "servicios"
        private const val COL_CLIENTE_ID = "cliente_id"
        private const val COL_CLIENTE_NOMBRE = "cliente_nombre"
        private const val COL_DIRECCION_SERVICIO = "direccion_servicio"
        private const val COL_TIPO_SERVICIO = "tipo_servicio"
        private const val COL_MATERIALES = "materiales"
        private const val COL_OBSERVACIONES = "observaciones"
        private const val COL_FOTOS = "fotos"
        private const val COL_FECHA_SERVICIO = "fecha_servicio"
        private const val COL_TECNICO_ID = "tecnico_id"
        private const val COL_PDF_URL = "pdf_url"
        private const val COL_ESTADO_SERVICIO = "estado_servicio"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla clientes (sin dirección)
        val createClientesTable = """
            CREATE TABLE $TABLE_CLIENTES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_EMAIL TEXT,
                $COL_ESTADO TEXT DEFAULT 'activo'
            )
        """.trimIndent()
        db.execSQL(createClientesTable)

        // Crear tabla servicios
        val createServiciosTable = """
            CREATE TABLE $TABLE_SERVICIOS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_CLIENTE_ID INTEGER NOT NULL,
                $COL_CLIENTE_NOMBRE TEXT NOT NULL,
                $COL_DIRECCION_SERVICIO TEXT,
                $COL_TIPO_SERVICIO TEXT,
                $COL_MATERIALES TEXT,
                $COL_OBSERVACIONES TEXT,
                $COL_FOTOS TEXT,
                $COL_FECHA_SERVICIO INTEGER,
                $COL_TECNICO_ID TEXT,
                $COL_PDF_URL TEXT,
                $COL_ESTADO_SERVICIO TEXT DEFAULT 'pendiente'
            )
        """.trimIndent()
        db.execSQL(createServiciosTable)

        // ✅ Crear tabla tecnicos
        val createTecnicosTable = """
            CREATE TABLE IF NOT EXISTS tecnicos (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                email TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                fecha_creacion INTEGER
            )
        """.trimIndent()
        db.execSQL(createTecnicosTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CLIENTES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SERVICIOS")
        db.execSQL("DROP TABLE IF EXISTS tecnicos")
        onCreate(db)
    }

    // === CLIENTES ===

    fun agregarCliente(cliente: Cliente): Long {
        val values = ContentValues().apply {
            put(COL_NOMBRE, cliente.nombre)
            put(COL_EMAIL, cliente.email)
            put(COL_ESTADO, cliente.estado)
        }
        return writableDatabase.insert(TABLE_CLIENTES, null, values)
    }

    fun obtenerClientesActivos(): List<Cliente> {
        val clientes = mutableListOf<Cliente>()
        val cursor = readableDatabase.query(
            TABLE_CLIENTES,
            null,
            "$COL_ESTADO = ?",
            arrayOf("activo"),
            null,
            null,
            "$COL_NOMBRE ASC"
        )
        while (cursor.moveToNext()) {
            val cliente = Cliente(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                nombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COL_EMAIL)),
                estado = cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO))
            )
            clientes.add(cliente)
        }
        cursor.close()
        return clientes
    }

    // === SERVICIOS ===

    fun agregarServicio(servicio: Servicio): Long {
        val fotosString = servicio.fotos.joinToString(",")
        val values = ContentValues().apply {
            put(COL_CLIENTE_ID, servicio.clienteId)
            put(COL_CLIENTE_NOMBRE, servicio.clienteNombre)
            put(COL_DIRECCION_SERVICIO, servicio.direccion)
            put(COL_TIPO_SERVICIO, servicio.tipoServicio)
            put(COL_MATERIALES, servicio.materiales)
            put(COL_OBSERVACIONES, servicio.observaciones)
            put(COL_FOTOS, fotosString)
            put(COL_FECHA_SERVICIO, servicio.fechaServicio)
            put(COL_TECNICO_ID, servicio.tecnicoId)
            put(COL_PDF_URL, servicio.pdfUrl)
            put(COL_ESTADO_SERVICIO, servicio.estado)
        }
        return writableDatabase.insert(TABLE_SERVICIOS, null, values)
    }

    fun obtenerServicios(): List<Servicio> {
        val servicios = mutableListOf<Servicio>()
        val cursor = readableDatabase.query(
            TABLE_SERVICIOS,
            null,
            null,
            null,
            null,
            null,
            "$COL_FECHA_SERVICIO DESC"
        )
        while (cursor.moveToNext()) {
            val fotosString = cursor.getString(cursor.getColumnIndexOrThrow(COL_FOTOS))
            val fotosList = if (fotosString.isNotEmpty()) fotosString.split(",") else emptyList()
            val servicio = Servicio(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                clienteId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CLIENTE_ID)),
                clienteNombre = cursor.getString(cursor.getColumnIndexOrThrow(COL_CLIENTE_NOMBRE)),
                direccion = cursor.getString(cursor.getColumnIndexOrThrow(COL_DIRECCION_SERVICIO)),
                tipoServicio = cursor.getString(cursor.getColumnIndexOrThrow(COL_TIPO_SERVICIO)),
                materiales = cursor.getString(cursor.getColumnIndexOrThrow(COL_MATERIALES)),
                observaciones = cursor.getString(cursor.getColumnIndexOrThrow(COL_OBSERVACIONES)),
                fotos = fotosList,
                fechaServicio = cursor.getLong(cursor.getColumnIndexOrThrow(COL_FECHA_SERVICIO)),
                tecnicoId = cursor.getString(cursor.getColumnIndexOrThrow(COL_TECNICO_ID)),
                pdfUrl = cursor.getString(cursor.getColumnIndexOrThrow(COL_PDF_URL)),
                estado = cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO_SERVICIO))
            )
            servicios.add(servicio)
        }
        cursor.close()
        return servicios
    }

    fun obtenerServicioPorId(id: Long): Servicio? {
        val cursor = readableDatabase.query(
            TABLE_SERVICIOS,
            null,
            "$COL_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        return cursor?.use {
            if (it.moveToFirst()) {
                val fotosString = it.getString(it.getColumnIndexOrThrow(COL_FOTOS))
                val fotosList = if (fotosString.isNotEmpty()) fotosString.split(",") else emptyList()
                Servicio(
                    id = it.getLong(it.getColumnIndexOrThrow(COL_ID)),
                    clienteId = it.getLong(it.getColumnIndexOrThrow(COL_CLIENTE_ID)),
                    clienteNombre = it.getString(it.getColumnIndexOrThrow(COL_CLIENTE_NOMBRE)),
                    direccion = it.getString(it.getColumnIndexOrThrow(COL_DIRECCION_SERVICIO)),
                    tipoServicio = it.getString(it.getColumnIndexOrThrow(COL_TIPO_SERVICIO)),
                    materiales = it.getString(it.getColumnIndexOrThrow(COL_MATERIALES)),
                    observaciones = it.getString(it.getColumnIndexOrThrow(COL_OBSERVACIONES)),
                    fotos = fotosList,
                    fechaServicio = it.getLong(it.getColumnIndexOrThrow(COL_FECHA_SERVICIO)),
                    tecnicoId = it.getString(it.getColumnIndexOrThrow(COL_TECNICO_ID)),
                    pdfUrl = it.getString(it.getColumnIndexOrThrow(COL_PDF_URL)),
                    estado = it.getString(it.getColumnIndexOrThrow(COL_ESTADO_SERVICIO))
                )
            } else null
        }
    }

    // === TECNICOS ===

    fun agregarTecnico(nombre: String, email: String, password: String): Long {
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("email", email)
            put("password", password)
            put("fecha_creacion", System.currentTimeMillis())
        }
        return writableDatabase.insert("tecnicos", null, values)
    }
}