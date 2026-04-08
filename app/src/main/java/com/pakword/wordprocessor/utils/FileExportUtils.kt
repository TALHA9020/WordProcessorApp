package com.pakword.wordprocessor.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.kernel.pdf.PdfDocument
import java.io.File
import java.io.FileOutputStream

object FileExportUtils {
    
    fun saveAsTxt(context: Context, text: String) {
        val fileName = "document_${System.currentTimeMillis()}.txt"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(text.toByteArray(Charsets.UTF_8))
            }
            showToast(context, "✅ TXT محفوظ: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(context, "❌ خرابی: ${e.message}")
        }
    }
    
    fun saveAsDoc(context: Context, htmlContent: String) {
        val fileName = "document_${System.currentTimeMillis()}.doc"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        val fullHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <title>Word Document</title>
                <style>
                    body { font-family: Arial, sans-serif; padding: 20px; direction: ltr; }
                </style>
            </head>
            <body>
                $htmlContent
            </body>
            </html>
        """.trimIndent()
        
        try {
            FileOutputStream(file).use { outputStream ->
                outputStream.write(fullHtml.toByteArray(Charsets.UTF_8))
            }
            showToast(context, "✅ DOC محفوظ: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(context, "❌ خرابی: ${e.message}")
        }
    }
    
    fun saveAsPdf(context: Context, htmlContent: String) {
        val fileName = "document_${System.currentTimeMillis()}.pdf"
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        
        try {
            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)
            
            // Convert HTML to plain text for PDF (simple stripping)
            val plainText = android.text.Html.fromHtml(htmlContent, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
            val paragraphs = plainText.split("\n")
            
            for (para in paragraphs) {
                if (para.isNotBlank()) {
                    document.add(Paragraph(para))
                }
            }
            
            document.close()
            showToast(context, "✅ PDF محفوظ: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(context, "❌ PDF خرابی: ${e.message}")
        }
    }
    
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}
