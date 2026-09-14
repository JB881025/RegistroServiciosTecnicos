package com.example.registroserviciostecnicos.utils

import android.os.AsyncTask
import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class MailSender {

    companion object {
        // ⚠️ CONFIGURA TU CORREO AQUÍ ⚠️
        private const val EMAIL = "jbingenieriasyt@gmail.com"
        private const val PASSWORD = "ippa rqdd daax tsur"   // ✅ Contraseña de aplicación actualizada
        private const val SMTP_HOST = "smtp.gmail.com"
        private const val SMTP_PORT = "587"
    }

    fun sendEmailWithAttachment(
        toEmail: String,
        subject: String,
        bodyHtml: String,
        pdfFile: File,
        onResult: (Boolean) -> Unit
    ) {
        SendMailTask(onResult).execute(toEmail, subject, bodyHtml, pdfFile.absolutePath)
    }

    private inner class SendMailTask(private val onResult: (Boolean) -> Unit) : AsyncTask<String, Void, Boolean>() {

        override fun doInBackground(vararg params: String): Boolean {
            return try {
                val toEmail = params[0]
                val subject = params[1]
                val bodyHtml = params[2]
                val pdfPath = params[3]

                val props = Properties().apply {
                    put("mail.smtp.host", SMTP_HOST)
                    put("mail.smtp.port", SMTP_PORT)
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                }

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(EMAIL, PASSWORD)
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(EMAIL))
                    setRecipient(Message.RecipientType.TO, InternetAddress(toEmail))
                    setSubject(subject)
                }

                val multipart = MimeMultipart()

                // ✅ Parte de texto (HTML)
                val htmlPart = MimeBodyPart().apply {
                    setContent(bodyHtml, "text/html; charset=utf-8")
                }
                multipart.addBodyPart(htmlPart)

                // ✅ Parte del archivo adjunto (PDF)
                val attachmentPart = MimeBodyPart().apply {
                    val source = FileDataSource(pdfPath)
                    dataHandler = DataHandler(source)
                    fileName = File(pdfPath).name
                }
                multipart.addBodyPart(attachmentPart)

                message.setContent(multipart)

                Transport.send(message)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        override fun onPostExecute(result: Boolean) {
            onResult(result)
        }
    }
}