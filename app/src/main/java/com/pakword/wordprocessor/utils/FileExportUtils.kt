package com.pakword.wordprocessor.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.itextpdf.kernel.font.PdfFontFactory
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.io.font.constants.StandardFonts
import java.io.File
import java.io.FileOutputStream

object FileExportUtils {
    
    fun saveAsTxt(context: Context, text: String) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "document_${System.currentTimeMillis()}.txt")
        try {
            FileOutputStream(file).use { it.write(text.toByteArray(Charsets.UTF_8)) }
            showToast(context, "✅ TXT محفوظ: ${file.absolutePath}")
        } catch (e: Exception) { showToast(context, "❌ خرابی: ${e.message}") }
    }
    
    fun saveAsDoc(context: Context, htmlContent: String) {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "document_${System.currentTimeMillis()}.doc")
        val fullHtml = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Word Document</title></head><body>$htmlContent</body></html>"
        try {
            FileOutputStream(file).use { it.write(fullHtml.toByteArray(Charsets.UTF_8)) }
            showToast(context, "✅ DOC محفوظ: ${file.absolutePath}")
        } catch (e: Exception) { showToast(context, "❌ خرابی: ${e.message}") }
    }
    
    fun saveAsPdf(context: Context, htmlContent: String, pageSize: String = "A4", marginType: String = "normal") {
        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "document_${System.currentTimeMillis()}.pdf")
        try {
            val writer = PdfWriter(file)
            val pdfDoc = PdfDocument(writer)
            val pageSizeEnum = when (pageSize) {
                "Legal" -> com.itextpdf.kernel.geom.PageSize.LEGAL
                "Letter" -> com.itextpdf.kernel.geom.PageSize.LETTER
                else -> com.itextpdf.kernel.geom.PageSize.A4
            }
            pdfDoc.setDefaultPageSize(pageSizeEnum)
            
            val font = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN)
            val document = Document(pdfDoc)
            val marginMap = mapOf("normal" to 72f, "narrow" to 36f, "custom" to 50f)
            val margin = marginMap[marginType] ?: 72f
            document.setMargins(margin, margin, margin, margin)
            
            val plainText = android.text.Html.fromHtml(htmlContent, android.text.Html.FROM_HTML_MODE_LEGACY).toString()
            val paragraphs = plainText.split("\n")
            for (para in paragraphs) {
                if (para.isNotBlank()) {
                    val paragraph = Paragraph(para).setFont(font)
                    document.add(paragraph)
                }
            }
            document.close()
            showToast(context, "✅ PDF محفوظ: ${file.absolutePath}")
        } catch (e: Exception) {
            e.printStackTrace()
            showToast(context, "❌ PDF خرابی: ${e.message}")
        }
    }
    
    private fun showToast(context: Context, msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}