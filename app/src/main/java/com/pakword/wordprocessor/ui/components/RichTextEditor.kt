package com.pakword.wordprocessor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material.RichTextEditor
import com.pakword.wordprocessor.utils.FileExportUtils
import kotlinx.coroutines.launch

class RichTextEditorViewModel : ViewModel() {
    val richTextState = RichTextState()
    
    fun toggleBold() {
        viewModelScope.launch {
            richTextState.toggleSpanStyle(
                androidx.compose.ui.text.SpanStyle(
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                )
            )
        }
    }
    
    fun toggleItalic() {
        viewModelScope.launch {
            richTextState.toggleSpanStyle(
                androidx.compose.ui.text.SpanStyle(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            )
        }
    }
    
    fun toggleUnderline() {
        viewModelScope.launch {
            richTextState.toggleSpanStyle(
                androidx.compose.ui.text.SpanStyle(
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                )
            )
        }
    }
    
    fun setTextDirection(isRtl: Boolean) {
        viewModelScope.launch {
            richTextState.toggleParagraphStyle(
                androidx.compose.ui.text.ParagraphStyle(
                    textDirection = if (isRtl) 
                        androidx.compose.ui.text.style.TextDirection.Rtl 
                    else 
                        androidx.compose.ui.text.style.TextDirection.Ltr
                )
            )
        }
    }
    
    fun setFontFamily(fontFamily: androidx.compose.ui.text.font.FontFamily) {
        viewModelScope.launch {
            richTextState.toggleSpanStyle(
                androidx.compose.ui.text.SpanStyle(fontFamily = fontFamily)
            )
        }
    }
    
    fun setFontSize(size: androidx.compose.ui.unit.TextUnit) {
        viewModelScope.launch {
            richTextState.toggleSpanStyle(
                androidx.compose.ui.text.SpanStyle(fontSize = size)
            )
        }
    }
    
    fun setTextColor(color: androidx.compose.ui.graphics.Color) {
        viewModelScope.launch {
            richTextState.toggleSpanStyle(
                androidx.compose.ui.text.SpanStyle(color = color)
            )
        }
    }
    
    fun setHighlightColor(color: androidx.compose.ui.graphics.Color) {
        viewModelScope.launch {
            richTextState.toggleSpanStyle(
                androidx.compose.ui.text.SpanStyle(background = color)
            )
        }
    }
    
    fun setLineSpacing(spacing: androidx.compose.ui.unit.TextUnit) {
        viewModelScope.launch {
            richTextState.toggleParagraphStyle(
                androidx.compose.ui.text.ParagraphStyle(lineHeight = spacing)
            )
        }
    }
    
    fun setFirstLineIndent(indent: androidx.compose.ui.unit.TextUnit) {
        viewModelScope.launch {
            richTextState.toggleParagraphStyle(
                androidx.compose.ui.text.ParagraphStyle(textIndent = indent)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RichTextEditor(viewModel: RichTextEditorViewModel) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val richTextState = viewModel.richTextState
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        FormattingToolbar(
            viewModel = viewModel,
            onExportTxt = {
                val text = richTextState.getPlainText()
                FileExportUtils.saveAsTxt(context, text)
            },
            onExportDoc = {
                val html = richTextState.toHtml()
                FileExportUtils.saveAsDoc(context, html)
            },
            onExportPdf = {
                val html = richTextState.toHtml()
                FileExportUtils.saveAsPdf(context, html)
            },
            onFontSelect = { fontFamily ->
                viewModel.setFontFamily(fontFamily)
            },
            onFontSizeSelect = { size ->
                viewModel.setFontSize(size)
            }
        )
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            RichTextEditor(
                state = richTextState,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
