package com.pakword.wordprocessor.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormattingToolbar(
    viewModel: RichTextEditorViewModel,
    onExportTxt: () -> Unit,
    onExportDoc: () -> Unit,
    onExportPdf: () -> Unit,
    onFontSelect: (FontFamily) -> Unit,
    onFontSizeSelect: (androidx.compose.ui.unit.TextUnit) -> Unit
) {
    var showFontDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showHighlightPicker by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Bold Button
        IconButton(onClick = { viewModel.toggleBold() }) {
            Icon(Icons.Default.FormatBold, contentDescription = "Bold")
        }
        
        // Italic Button
        IconButton(onClick = { viewModel.toggleItalic() }) {
            Icon(Icons.Default.FormatItalic, contentDescription = "Italic")
        }
        
        // Underline Button
        IconButton(onClick = { viewModel.toggleUnderline() }) {
            Icon(Icons.Default.FormatUnderlined, contentDescription = "Underline")
        }
        
        // Font Family Button
        IconButton(onClick = { showFontDialog = true }) {
            Icon(Icons.Default.FontDownload, contentDescription = "Font")
        }
        
        // Font Size Button with counter
        var fontSize by remember { mutableStateOf(16.sp) }
        val fontSizes = listOf(12.sp, 14.sp, 16.sp, 18.sp, 20.sp, 24.sp, 28.sp, 32.sp)
        
        TextButton(
            onClick = { 
                val currentIndex = fontSizes.indexOf(fontSize)
                val newIndex = (currentIndex + 1) % fontSizes.size
                fontSize = fontSizes[newIndex]
                onFontSizeSelect(fontSize)
            }
        ) {
            Text("${fontSize.value.toInt()}px")
        }
        
        // Text Color Button
        IconButton(onClick = { showColorPicker = true }) {
            Icon(Icons.Default.FormatColorText, contentDescription = "Text Color")
        }
        
        // Highlight Button
        IconButton(onClick = { showHighlightPicker = true }) {
            Icon(Icons.Default.Highlight, contentDescription = "Highlight")
        }
        
        // RTL Button
        IconButton(onClick = { viewModel.setTextDirection(true) }) {
            Text("RTL", fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }
        
        // LTR Button
        IconButton(onClick = { viewModel.setTextDirection(false) }) {
            Text("LTR", fontSize = 12.sp)
        }
        
        // Line Spacing Button
        IconButton(onClick = { 
            viewModel.setLineSpacing(24.sp)
        }) {
            Icon(Icons.Default.FormatLineSpacing, contentDescription = "Line Spacing")
        }
        
        // First Line Indent Button
        IconButton(onClick = { 
            viewModel.setFirstLineIndent(32.sp)
        }) {
            Text("📏", fontSize = 20.sp)
        }
        
        // Export Buttons
        IconButton(onClick = onExportTxt) {
            Text("TXT", fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        }
        
        IconButton(onClick = onExportDoc) {
            Text("DOC", fontSize = 12.sp)
        }
        
        IconButton(onClick = onExportPdf) {
            Text("PDF", fontSize = 12.sp)
        }
    }
    
    // Font Selection Dialog
    if (showFontDialog) {
        FontSelectionDialog(
            onDismiss = { showFontDialog = false },
            onFontSelected = { fontFamily ->
                onFontSelect(fontFamily)
                showFontDialog = false
            }
        )
    }
    
    // Color Picker Dialog (simple)
    if (showColorPicker) {
        ColorPickerDialog(
            title = "فونٹ کلر منتخب کریں",
            onDismiss = { showColorPicker = false },
            onColorSelected = { color ->
                viewModel.setTextColor(color)
                showColorPicker = false
            }
        )
    }
    
    // Highlight Picker Dialog
    if (showHighlightPicker) {
        ColorPickerDialog(
            title = "ہائی لائٹ کلر منتخب کریں",
            onDismiss = { showHighlightPicker = false },
            onColorSelected = { color ->
                viewModel.setHighlightColor(color)
                showHighlightPicker = false
            }
        )
    }
}

@Composable
fun ColorPickerDialog(
    title: String,
    onDismiss: () -> Unit,
    onColorSelected: (Color) -> Unit
) {
    val colors = listOf(
        Color.Black, Color.Red, Color.Blue, Color.Green,
        Color.Yellow, Color.Magenta, Color.Cyan, Color.Gray,
        Color(0xFFFF5722), Color(0xFF9C27B0), Color(0xFF4CAF50)
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                Text("رنگ منتخب کریں:")
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    colors.forEach { color ->
                        IconButton(
                            onClick = { onColorSelected(color) },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(color, shape = MaterialTheme.shapes.small)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("منسوخ")
            }
        }
    )
}

@Composable
fun FontSelectionDialog(
    onDismiss: () -> Unit,
    onFontSelected: (FontFamily) -> Unit
) {
    val fontList = listOf(
        "System Default" to FontFamily.Default,
        "Sans Serif" to FontFamily.SansSerif,
        "Serif" to FontFamily.Serif,
        "Monospace" to FontFamily.Monospace,
        "Cursive" to FontFamily.Cursive
    )
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("فونٹ منتخب کریں") },
        text = {
            Column {
                fontList.forEach { (name, fontFamily) ->
                    TextButton(
                        onClick = { onFontSelected(fontFamily) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = name,
                            fontFamily = fontFamily,
                            fontSize = 18.sp
                        )
                    }
                    Divider()
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("منسوخ")
            }
        }
    )
}
