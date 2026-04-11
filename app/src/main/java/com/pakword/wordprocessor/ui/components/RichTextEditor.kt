package com.pakword.wordprocessor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichTextEditor
import com.pakword.wordprocessor.utils.FileExportUtils
import com.pakword.wordprocessor.utils.FontManager
import kotlinx.coroutines.launch

class RichTextEditorViewModel : ViewModel() {
    val richTextState = RichTextState()
    
    fun toggleBold() = viewModelScope.launch {
        richTextState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold))
    }
    fun toggleItalic() = viewModelScope.launch {
        richTextState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic))
    }
    fun toggleUnderline() = viewModelScope.launch {
        richTextState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline))
    }
    fun setTextDirection(isRtl: Boolean) = viewModelScope.launch {
        richTextState.toggleParagraphStyle(androidx.compose.ui.text.ParagraphStyle(
            textDirection = if (isRtl) androidx.compose.ui.text.style.TextDirection.Rtl else androidx.compose.ui.text.style.TextDirection.Ltr
        ))
    }
    fun setFontFamily(fontFamily: androidx.compose.ui.text.font.FontFamily) = viewModelScope.launch {
        richTextState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(fontFamily = fontFamily))
    }
    fun setFontSize(size: TextUnit) = viewModelScope.launch {
        richTextState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(fontSize = size))
    }
    fun setTextColor(color: androidx.compose.ui.graphics.Color) = viewModelScope.launch {
        richTextState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(color = color))
    }
    fun setHighlightColor(color: androidx.compose.ui.graphics.Color) = viewModelScope.launch {
        richTextState.toggleSpanStyle(androidx.compose.ui.text.SpanStyle(background = color))
    }
    fun setLineSpacing(lineHeight: TextUnit) = viewModelScope.launch {
        richTextState.toggleParagraphStyle(androidx.compose.ui.text.ParagraphStyle(lineHeight = lineHeight))
    }
    fun setFirstLineIndent(indent: TextUnit) = viewModelScope.launch {
        richTextState.toggleParagraphStyle(androidx.compose.ui.text.ParagraphStyle(textIndent = indent))
    }
    
    var pageSize: String by mutableStateOf("A4")
    var marginType: String by mutableStateOf("normal")
    fun setPageSize(size: String) { pageSize = size }
    fun setMarginType(type: String) { marginType = type }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichTextEditor(viewModel: RichTextEditorViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val richTextState = viewModel.richTextState
    
    Column(modifier = Modifier.fillMaxSize()) {
        FormattingToolbar(
            viewModel = viewModel,
            onExportTxt = { 
                val text = richTextState.text.toString()
                FileExportUtils.saveAsTxt(context, text)
            },
            onExportDoc = { 
                val html = richTextState.toHtml()
                FileExportUtils.saveAsDoc(context, html)
            },
            onExportPdf = { 
                val html = richTextState.toHtml()
                FileExportUtils.saveAsPdf(context, html, viewModel.pageSize, viewModel.marginType)
            },
            onFontSelect = { viewModel.setFontFamily(it) },
            onFontSizeSelect = { viewModel.setFontSize(it) },
            onCustomFontUpload = { uri ->
                FontManager.loadCustomFont(context, uri) { fontFamily ->
                    viewModel.setFontFamily(fontFamily)
                }
            },
            onLineSpacingChange = { viewModel.setLineSpacing(it) },
            onParagraphSpacingChange = { },
            onPageSettingsClick = { }
        )
        
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            RichTextEditor(
                state = richTextState,
                modifier = Modifier.fillMaxSize().verticalScroll(scrollState),
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}