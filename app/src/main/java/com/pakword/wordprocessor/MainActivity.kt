package com.pakword.wordprocessor

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pakword.wordprocessor.ui.components.RichTextEditor
import com.pakword.wordprocessor.ui.components.RichTextEditorViewModel
import com.pakword.wordprocessor.ui.theme.WordProcessorTheme

class MainActivity : ComponentActivity() {

    // Permission launcher for Android 6+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (!allGranted) {
            // Optionally show a rationale
            android.widget.Toast.makeText(
                this,
                "فائلز محفوظ کرنے کے لیے اجازت درکار ہے",
                android.widget.Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()
        setContent {
            WordProcessorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WordProcessorApp()
                }
            }
        }
    }

    private fun checkAndRequestPermissions() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.READ_MEDIA_IMAGES) // For Android 13+ (storage simplified)
        } else {
            listOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
        val needToRequest = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }
        if (needToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(needToRequest.toTypedArray())
        }
    }

    @Composable
    fun WordProcessorApp() {
        val viewModel: RichTextEditorViewModel = viewModel()
        RichTextEditor(viewModel = viewModel)
    }
}