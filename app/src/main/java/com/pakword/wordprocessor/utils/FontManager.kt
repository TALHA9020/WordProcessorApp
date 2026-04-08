package com.pakword.wordprocessor.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.toFontFamily
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

object FontManager {
    private var customTypeface: Typeface? = null
    private var customFontFamily: FontFamily? = null

    fun loadCustomFont(context: Context, uri: Uri, onComplete: (FontFamily) -> Unit) {
        try {
            val file = copyFontToCache(context, uri)
            val typeface = Typeface.createFromFile(file)
            customTypeface = typeface
            customFontFamily = typeface.toFontFamily()
            onComplete(customFontFamily!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun copyFontToCache(context: Context, uri: Uri): File {
        val contentResolver: ContentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri) ?: throw Exception("Cannot open stream")
        val fileName = "custom_font_${System.currentTimeMillis()}.ttf"
        val cacheFile = File(context.cacheDir, fileName)
        FileOutputStream(cacheFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        inputStream.close()
        return cacheFile
    }

    fun getCustomFontFamily(): FontFamily? = customFontFamily
}