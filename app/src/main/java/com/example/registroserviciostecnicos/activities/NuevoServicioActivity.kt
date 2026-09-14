package com.example.registroserviciostecnicos.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.example.registroserviciostecnicos.R
import com.example.registroserviciostecnicos.models.Servicio
import com.example.registroserviciostecnicos.utils.DatabaseHelper
import com.example.registroserviciostecnicos.utils.MailSender
import com.google.android.material.textfield.TextInputEditText
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.SolidBorder
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class NuevoServicioActivity : AppCompatActivity() {

    private lateinit var btnSeleccionarCliente: Button
    private lateinit var tvClienteSeleccionado: TextView
    private lateinit var spTipoServicio: Spinner
    private lateinit var etDescripcion: TextInputEditText
    private lateinit var btnTomarFoto: Button
    private lateinit var btnAdjuntarFoto: Button
    private lateinit var tvCantidadFotos: TextView
    private lateinit var btnGuardar: Button

    private lateinit var dbHelper: DatabaseHelper

    private var clienteId: Long = 0
    private var clienteNombre: String = ""
    private var clienteEmail: String = ""

    private val fotosUris = mutableListOf<Uri>()
    private val fotosPaths = mutableListOf<String>()

    private val tiposServicio = arrayOf(
        "Servicio Tecnico",
        "Mantenimiento",
        "Obra en Ejecucion",
        "Otro"
    )

    // Colores corporativos
    private val colorPrimary = DeviceRgb(26, 35, 126)   // #1A237E
    private val colorAzulSuave = DeviceRgb(41, 98, 150) // #296296 - Azul suave para fondos
    private val colorRojo = DeviceRgb(200, 0, 0)        // Rojo para la línea
    private val colorLightGray = DeviceRgb(245, 245, 245) // Gris claro

    private val selectClienteLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            clienteId = data?.getLongExtra("cliente_id", 0) ?: 0
            clienteNombre = data?.getStringExtra("cliente_nombre") ?: ""
            clienteEmail = data?.getStringExtra("cliente_email") ?: ""

            if (clienteId > 0) {
                tvClienteSeleccionado.text = "✓ $clienteNombre"
                tvClienteSeleccionado.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
            }
        }
    }

    private val tomarFotoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            val photoUri = data?.data
            photoUri?.let {
                fotosUris.add(it)
                actualizarVistaFotos()
                val realPath = getRealPathFromUri(it)
                realPath?.let { path -> fotosPaths.add(path) }
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            fotosUris.add(it)
            actualizarVistaFotos()
            val realPath = getRealPathFromUri(it)
            realPath?.let { path -> fotosPaths.add(path) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nuevo_servicio)

        dbHelper = DatabaseHelper(this)

        btnSeleccionarCliente = findViewById(R.id.btnSeleccionarCliente)
        tvClienteSeleccionado = findViewById(R.id.tvClienteSeleccionado)
        spTipoServicio = findViewById(R.id.spTipoServicio)
        etDescripcion = findViewById(R.id.etDescripcion)
        btnTomarFoto = findViewById(R.id.btnTomarFoto)
        btnAdjuntarFoto = findViewById(R.id.btnAdjuntarFoto)
        tvCantidadFotos = findViewById(R.id.tvCantidadFotos)
        btnGuardar = findViewById(R.id.btnGuardar)

        setupSpinner()
        setupListeners()
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiposServicio)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spTipoServicio.adapter = adapter
    }

    private fun setupListeners() {
        btnSeleccionarCliente.setOnClickListener {
            val intent = Intent(this, ClientesActivity::class.java)
            intent.putExtra("seleccionar", true)
            selectClienteLauncher.launch(intent)
        }

        btnTomarFoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (intent.resolveActivity(packageManager) != null) {
                tomarFotoLauncher.launch(intent)
            } else {
                Toast.makeText(this, "No se puede abrir la camara", Toast.LENGTH_SHORT).show()
            }
        }

        btnAdjuntarFoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnGuardar.setOnClickListener {
            guardarServicio()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun getRealPathFromUri(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            it.getString(columnIndex)
        }
    }

    private fun actualizarVistaFotos() {
        tvCantidadFotos.text = "${fotosUris.size} fotos adjuntas"
    }

    private fun getLogoImage(): Image? {
        return try {
            val drawable = ResourcesCompat.getDrawable(resources, R.drawable.logo, null)
            drawable?.let {
                val bitmap = Bitmap.createBitmap(it.intrinsicWidth, it.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                it.setBounds(0, 0, canvas.width, canvas.height)
                it.draw(canvas)
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val imageData = ImageDataFactory.create(stream.toByteArray())
                val image = Image(imageData)
                image.scaleToFit(97.32f, 129.76f)
                image
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun getTituloReporte(tipoServicio: String): String {
        return when (tipoServicio) {
            "Servicio Tecnico" -> "REPORTE DE SERVICIO TECNICO"
            "Mantenimiento" -> "REPORTE DE MANTENIMIENTO"
            "Obra en Ejecucion" -> "REPORTE DE OBRA EN EJECUCION"
            "Otro" -> "REPORTE DE SERVICIO"
            else -> "REPORTE DE SERVICIO TECNICO"
        }
    }

    private fun guardarServicio() {
        if (clienteId == 0L) {
            Toast.makeText(this, "Selecciona un cliente", Toast.LENGTH_LONG).show()
            return
        }

        val descripcion = etDescripcion.text.toString().trim()
        if (descripcion.isEmpty()) {
            Toast.makeText(this, "Ingresa la descripcion de las actividades", Toast.LENGTH_LONG).show()
            etDescripcion.requestFocus()
            return
        }

        if (fotosPaths.isEmpty()) {
            Toast.makeText(this, "Debes adjuntar al menos una foto", Toast.LENGTH_LONG).show()
            return
        }

        val tipoServicio = spTipoServicio.selectedItem.toString()

        val servicio = Servicio(
            clienteId = clienteId,
            clienteNombre = clienteNombre,
            direccion = "",
            tipoServicio = tipoServicio,
            materiales = "",
            observaciones = descripcion,
            fotos = fotosPaths,
            tecnicoId = "tecnico_1",
            estado = "pendiente"
        )

        val id = dbHelper.agregarServicio(servicio)
        if (id > 0) {
            Toast.makeText(this, "Servicio guardado correctamente", Toast.LENGTH_SHORT).show()
            generarPDFMejorado(servicio)
            finish()
        } else {
            Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
        }
    }

    // ✅ PDF MEJORADO - LOGO CENTRADO ARRIBA, NOMBRE A LA IZQUIERDA
    private fun generarPDFMejorado(servicio: Servicio) {
        try {
            val byteArrayOutputStream = ByteArrayOutputStream()
            val writer = PdfWriter(byteArrayOutputStream)
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc, PageSize.A4)
            document.setMargins(40f, 40f, 40f, 40f)

            val tituloReporte = getTituloReporte(servicio.tipoServicio)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

            // ============ ENCABEZADO ============
            // Tabla de 2 columnas: Logo centrado arriba (ocupa ambas columnas), nombre a la izquierda abajo
            val headerTable = Table(UnitValue.createPercentArray(2)).useAllAvailableWidth()
            headerTable.setFontSize(12f)

            // Fila 1: Logo (centrado, ocupa 2 columnas)
            val logoImage = getLogoImage()
            val logoCell = if (logoImage != null) {
                Cell(1, 2).add(logoImage)  // 1 fila, 2 columnas
                    .setBorder(Border.NO_BORDER)
                    .setPadding(0f)
                    .setTextAlignment(TextAlignment.CENTER)
            } else {
                Cell(1, 2).add(Paragraph("").setBorder(Border.NO_BORDER))
            }
            headerTable.addCell(logoCell)

            // Fila 2: Nombre de la empresa (izquierda)
            val nameCell = Cell().add(
                Paragraph("JB INGENIERIA S.A.S")
                    .setFontSize(18f)
                    .setBold()
                    .setFontColor(colorPrimary)
                    .setTextAlignment(TextAlignment.LEFT)
            ).setBorder(Border.NO_BORDER)
                .setPadding(0f)
            headerTable.addCell(nameCell)

            // Celda vacía a la derecha para mantener la estructura
            val emptyCell = Cell().add(Paragraph(""))
                .setBorder(Border.NO_BORDER)
            headerTable.addCell(emptyCell)

            document.add(headerTable)

            // Línea decorativa en ROJO
            document.add(Paragraph("_________________________________________________________")
                .setFontSize(12f)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontColor(colorRojo))

            document.add(Paragraph(" "))

            // Título del reporte
            document.add(Paragraph(tituloReporte)
                .setFontSize(20f)
                .setBold()
                .setFontColor(colorPrimary)
                .setTextAlignment(TextAlignment.CENTER))

            document.add(Paragraph(" "))

            // ============ DATOS DEL SERVICIO ============
            val dataTable = Table(UnitValue.createPercentArray(2)).useAllAvailableWidth()
            dataTable.setFontSize(11f)
            dataTable.setBackgroundColor(colorLightGray)
            dataTable.setBorder(SolidBorder(colorAzulSuave, 1f))

            // Etiquetas con azul suave
            val labelBg = colorAzulSuave
            val labelTextColor = ColorConstants.WHITE

            // Cliente
            val clienteLabel = Cell().add(Paragraph("CLIENTE:").setBold().setFontColor(labelTextColor))
                .setBackgroundColor(labelBg)
                .setPadding(6f)
            dataTable.addCell(clienteLabel)
            dataTable.addCell(Cell().add(Paragraph(servicio.clienteNombre)).setPadding(6f))

            // Tipo de Servicio
            val tipoLabel = Cell().add(Paragraph("TIPO DE SERVICIO:").setBold().setFontColor(labelTextColor))
                .setBackgroundColor(labelBg)
                .setPadding(6f)
            dataTable.addCell(tipoLabel)
            dataTable.addCell(Cell().add(Paragraph(servicio.tipoServicio)).setPadding(6f))

            // Descripción
            val descLabel = Cell().add(Paragraph("DESCRIPCION:").setBold().setFontColor(labelTextColor))
                .setBackgroundColor(labelBg)
                .setPadding(6f)
            dataTable.addCell(descLabel)
            dataTable.addCell(Cell().add(Paragraph(servicio.observaciones)).setPadding(6f))

            // Fecha
            val fechaLabel = Cell().add(Paragraph("FECHA:").setBold().setFontColor(labelTextColor))
                .setBackgroundColor(labelBg)
                .setPadding(6f)
            dataTable.addCell(fechaLabel)
            dataTable.addCell(Cell().add(Paragraph(dateFormat.format(Date(servicio.fechaServicio)))).setPadding(6f))

            document.add(dataTable)
            document.add(Paragraph(" "))

            // ============ FOTOS (4 COLUMNAS) ============
            if (servicio.fotos.isNotEmpty()) {
                document.add(Paragraph("FOTOS ADJUNTAS:")
                    .setFontSize(14f)
                    .setBold()
                    .setFontColor(colorPrimary))
                document.add(Paragraph(" "))

                val photoTable = Table(UnitValue.createPercentArray(4)).useAllAvailableWidth()
                photoTable.setFontSize(9f)

                for (fotoPath in servicio.fotos) {
                    try {
                        val imageData = ImageDataFactory.create(fotoPath)
                        val image = Image(imageData)
                        image.setWidth(100f)
                        image.setHeight(100f)
                        image.setAutoScale(true)

                        val imageCell = Cell().add(image)
                            .setPadding(4f)
                            .setBorder(SolidBorder(colorLightGray, 0.5f))
                            .setBackgroundColor(ColorConstants.WHITE)
                        photoTable.addCell(imageCell)
                    } catch (_: Exception) {
                        val errorCell = Cell().add(
                            Paragraph("Error")
                                .setFontColor(ColorConstants.RED)
                                .setFontSize(8f)
                        ).setPadding(4f)
                        photoTable.addCell(errorCell)
                    }
                }

                document.add(photoTable)
            } else {
                document.add(Paragraph("No se adjuntaron fotos."))
            }

            document.add(Paragraph(" "))

            // ============ PIE DE PAGINA ============
            val footerTable = Table(UnitValue.createPercentArray(1)).useAllAvailableWidth()
            footerTable.setMarginTop(20f)

            val footerCell = Cell().add(
                Paragraph("JB INGENIERIA S.A.S - Comprometidos con la calidad")
                    .setFontSize(10f)
                    .setBold()
                    .setFontColor(colorPrimary)
                    .setTextAlignment(TextAlignment.CENTER)
            ).setBorder(Border.NO_BORDER)
            footerTable.addCell(footerCell)

            val phoneCell = Cell().add(
                Paragraph("Celular: 3168756109 | Email: comercial@jbingenieria.com.co")
                    .setFontSize(9f)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.GRAY)
            ).setBorder(Border.NO_BORDER)
            footerTable.addCell(phoneCell)

            document.add(footerTable)

            document.close()

            val pdfBytes = byteArrayOutputStream.toByteArray()
            enviarCorreoConPDF(pdfBytes, servicio, tituloReporte)

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al generar PDF: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun enviarCorreoConPDF(pdfBytes: ByteArray, servicio: Servicio, tituloReporte: String) {
        try {
            val email = if (clienteEmail.isNotEmpty()) clienteEmail else "cliente@ejemplo.com"

            val dateFormat = SimpleDateFormat("ddMMyyyy_HHmm", Locale.getDefault())
            val fechaStr = dateFormat.format(Date(servicio.fechaServicio))
            val nombrePDF = "${servicio.clienteNombre.replace(" ", "_")}_$fechaStr.pdf"

            val tempFile = File(cacheDir, nombrePDF)
            tempFile.writeBytes(pdfBytes)

            val htmlBody = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>$tituloReporte</title>
                <style>
                    body { font-family: 'Segoe UI', Arial, sans-serif; color: #333333; background-color: #f9f9f9; margin: 0; padding: 20px; }
                    .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); overflow: hidden; }
                    .header { background: #1A237E; color: white; padding: 20px; text-align: center; }
                    .header h1 { margin: 0; font-size: 24px; font-weight: 700; letter-spacing: 1px; }
                    .header p { margin: 5px 0 0 0; font-size: 14px; opacity: 0.9; }
                    .content { padding: 30px 25px; }
                    .content h2 { color: #1A237E; font-size: 18px; margin-top: 0; border-bottom: 2px solid #FF6F00; padding-bottom: 10px; }
                    .content p { line-height: 1.6; margin: 12px 0; }
                    .detail-box { background-color: #f5f7fa; border-left: 4px solid #FF6F00; padding: 15px 20px; border-radius: 6px; margin: 15px 0; }
                    .detail-box .label { font-weight: bold; color: #1A237E; display: inline-block; min-width: 120px; }
                    .detail-box .value { color: #333333; }
                    .contact-box { margin-top: 20px; padding: 15px; background-color: #f5f7fa; border-radius: 8px; text-align: center; border: 1px solid #e0e0e0; }
                    .contact-box p { margin: 5px 0; }
                    .accent-text { color: #FF6F00; font-weight: bold; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>JB INGENIERIA S.A.S</h1>
                        <p>$tituloReporte</p>
                    </div>
                    <div class="content">
                        <h2>Estimado(a) ${servicio.clienteNombre}</h2>
                        <p>Por medio de la presente, adjuntamos el reporte detallado del servicio realizado:</p>

                        <div class="detail-box">
                            <p><span class="label">Tipo de Servicio:</span> <span class="value">${servicio.tipoServicio}</span></p>
                            <p><span class="label">Fecha:</span> <span class="value">${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date(servicio.fechaServicio))}</span></p>
                            <p><span class="label">Descripción:</span> <span class="value">${servicio.observaciones}</span></p>
                        </div>

                        <p>Adjunto encontrará el reporte completo en formato PDF.</p>
                        <p>Si tiene alguna duda o requiere información adicional, no dude en contactarnos.</p>

                        <p style="color: #1A237E; font-weight: bold; font-size: 16px;">¡Gracias por confiar en <span class="accent-text">JB INGENIERIA S.A.S</span>!</p>

                        <div class="contact-box">
                            <p style="font-weight: bold; color: #1A237E;">Contáctanos</p>
                            <p>Celular: <strong style="color: #FF6F00;">3168756109</strong></p>
                            <p>Email: <strong style="color: #1A237E;">comercial@jbingenieria.com.co</strong></p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """.trimIndent()

            val mailSender = MailSender()
            mailSender.sendEmailWithAttachment(email, "$tituloReporte - ${servicio.clienteNombre}", htmlBody, tempFile) { success ->
                runOnUiThread {
                    if (success) {
                        Toast.makeText(this, "Correo enviado a $email", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Error al enviar correo. Verifica tu conexion y credenciales.", Toast.LENGTH_LONG).show()
                    }
                }
            }

            tempFile.deleteOnExit()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error al enviar correo: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}