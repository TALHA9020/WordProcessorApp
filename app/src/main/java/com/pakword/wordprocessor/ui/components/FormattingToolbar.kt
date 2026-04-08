package com.pakword.wordprocessor.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    onFontSizeSelect: (androidx.compose.ui.unit.TextUnit) -> Unit,
    onCustomFontUpload: (Uri) -> Unit,
    onLineSpacingChange: (androidx.compose.ui.unit.TextUnit) -> Unit,
    onParagraphSpacingChange: (androidx.compose.ui.unit.TextUnit) -> Unit,
    onPageSettingsClick: () -> Unit
) {
    var showFontDialog by remember { mutableStateOf(false) }
    var showColorPicker by remember { mutableStateOf(false) }
    var showHighlightPicker by remember { mutableStateOf(false) }
    var showPageSettingsDialog by remember { mutableStateOf(false) }
    var showLineSpacingDialog by remember { mutableStateOf(false) }
    var showParaSpacingDialog by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val fontPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onCustomFontUpload(it) }
    }
    
    Row(
        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Bold, Italic, Underline
        IconButton(onClick = { viewModel.toggleBold() }) { Icon(Icons.Default.FormatBold, null) }
        IconButton(onClick = { viewModel.toggleItalic() }) { Icon(Icons.Default.FormatItalic, null) }
        IconButton(onClick = { viewModel.toggleUnderline() }) { Icon(Icons.Default.FormatUnderlined, null) }
        
        // Font family & size
        IconButton(onClick = { showFontDialog = true }) { Icon(Icons.Default.FontDownload, null) }
        
        var fontSize by remember { mutableStateOf(16.sp) }
        val fontSizes = listOf(12.sp, 14.sp, 16.sp, 18.sp, 20.sp, 24.sp, 28.sp, 32.sp)
        TextButton(onClick = {
            val idx = (fontSizes.indexOf(fontSize) + 1) % fontSizes.size
            fontSize = fontSizes[idx]
            onFontSizeSelect(fontSize)
        }) { Text("${fontSize.value.toInt()}px") }
        
        // Color & highlight
        IconButton(onClick = { showColorPicker = true }) { Icon(Icons.Default.FormatColorText, null) }
        IconButton(onClick = { showHighlightPicker = true }) { Icon(Icons.Default.Highlight, null) }
        
        // RTL/LTR
        IconButton(onClick = { viewModel.setTextDirection(true) }) { Text("RTL", fontSize = 12.sp) }
        IconButton(onClick = { viewModel.setTextDirection(false) }) { Text("LTR", fontSize = 12.sp) }
        
        // Line spacing
        IconButton(onClick = { showLineSpacingDialog = true }) { Icon(Icons.Default.FormatLineSpacing, null) }
        
        // Paragraph spacing
        IconButton(onClick = { showParaSpacingDialog = true }) { Text("¶", fontSize = 16.sp) }
        
        // First line indent
        IconButton(onClick = { viewModel.setFirstLineIndent(32.sp) }) { Text("📏", fontSize = 20.sp) }
        
        // Page settings
        IconButton(onClick = { showPageSettingsDialog = true }) { Icon(Icons.Default.Settings, null) }
        
        // Custom font upload
        IconButton(onClick = { fontPickerLauncher.launch("application/*") }) { Icon(Icons.Default.FileUpload, null) }
        
        // Export
        IconButton(onClick = onExportTxt) { Text("TXT") }
        IconButton(onClick = onExportDoc) { Text("DOC") }
        IconButton(onClick = onExportPdf) { Text("PDF") }
    }
    
    // Dialogs
    if (showFontDialog) FontSelectionDialog(onDismiss = { showFontDialog = false }, onFontSelected = onFontSelect)
    if (showColorPicker) ColorPickerDialog("فونٹ کلر", { showColorPicker = false }) { viewModel.setTextColor(it) }
    if (showHighlightPicker) ColorPickerDialog("ہائی لائٹ کلر", { showHighlightPicker = false }) { viewModel.setHighlightColor(it) }
    if (showPageSettingsDialog) PageSettingsDialog(
        currentPageSize = viewModel.pageSize,
        currentMargin = viewModel.marginType,
        onDismiss = { showPageSettingsDialog = false },
        onApply = { pageSize, margin ->
            viewModel.setPageSize(pageSize)
            viewModel.setMarginType(margin)
            showPageSettingsDialog = false
        }
    )
    if (showLineSpacingDialog) LineSpacingDialog(
        onDismiss = { showLineSpacingDialog = false },
        onSelect = { spacing -> onLineSpacingChange(spacing) }
    )
    if (showParaSpacingDialog) ParagraphSpacingDialog(
        onDismiss = { showParaSpacingDialog = false },
        onSelect = { spacing -> onParagraphSpacingChange(spacing) }
    )
}

@Composable
fun PageSettingsDialog(
    currentPageSize: String,
    currentMargin: String,
    onDismiss: () -> Unit,
    onApply: (String, String) -> Unit
) {
    var selectedPageSize by remember { mutableStateOf(currentPageSize) }
    var selectedMargin by remember { mutableStateOf(currentMargin) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("پیج سیٹنگز") },
        text = {
            Column {
                Text("پیج سائز:")
                Row {
                    listOf("A4", "Legal", "Letter").forEach { size ->
                        FilterChip(
                            selected = selectedPageSize == size,
                            onClick = { selectedPageSize = size },
                            label = { Text(size) },
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("مارجن:")
                Row {
                    listOf("normal" to "نارمل", "narrow" to "تنگ", "custom" to "کسٹم").forEach { (value, label) ->
                        FilterChip(
                            selected = selectedMargin == value,
                            onClick = { selectedMargin = value },
                            label = { Text(label) },
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = { onApply(selectedPageSize, selectedMargin) }) { Text("لاگو کریں") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("منسوخ") } }
    )
}

@Composable
fun LineSpacingDialog(onDismiss: () -> Unit, onSelect: (androidx.compose.ui.unit.TextUnit) -> Unit) {
    val options = listOf(1.0f, 1.15f, 1.5f, 2.0f)
    var selected by remember { mutableStateOf(1.5f) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("لائن سپیسنگ") },
        text = {
            Column {
                options.forEach { spacing ->
                    TextButton(onClick = { selected = spacing; onSelect(spacing.sp) }) {
                        Text("$spacing")
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("ٹھیک ہے") } }
    )
}

@Composable
fun ParagraphSpacingDialog(onDismiss: () -> Unit, onSelect: (androidx.compose.ui.unit.TextUnit) -> Unit) {
    val options = listOf(0, 4, 8, 12, 16, 24)
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("پیراگراف سپیسنگ (px)") },
        text = {
            Column {
                options.forEach { px ->
                    TextButton(onClick = { onSelect(px.sp) }) {
                        Text("$px px")
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text("ٹھیک ہے") } }
    )
}