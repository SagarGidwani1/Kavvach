package com.example.kavvach.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.kavvach.ai.OnnxModelRunner
import com.example.kavvach.audio.AudioRecorder
import com.example.kavvach.utils.AudioUtils
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var audioRecorder: AudioRecorder
    private lateinit var onnxModelRunner: OnnxModelRunner

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            startAnalysis()
        } else {
            Toast.makeText(this, "Microphone permission is required", Toast.LENGTH_SHORT).show()
        }
    }
    
    // UI States
    private val statusText = mutableStateOf("Idle")
    private val isAnalyzing = mutableStateOf(false)
    private val resultScore = mutableStateOf<Float?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        audioRecorder = AudioRecorder()
        try {
            onnxModelRunner = OnnxModelRunner(this)
        } catch (e: Exception) {
            e.printStackTrace()
            statusText.value = "Error loading model. Add voice_detector.onnx to assets folder."
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResultView(
                        statusText = statusText.value,
                        resultScore = resultScore.value,
                        isAnalyzing = isAnalyzing.value,
                        onAnalyzeClick = { checkPermissionAndStart() }
                    )
                }
            }
        }
    }

    private fun checkPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            startAnalysis()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    private fun startAnalysis() {
        if (isAnalyzing.value) return
        
        isAnalyzing.value = true
        resultScore.value = null
        
        lifecycleScope.launch {
            try {
                statusText.value = "Recording... (5 seconds)"
                val audioData = audioRecorder.recordAudio()
                
                statusText.value = "Analyzing..."
                val floatData = AudioUtils.shortArrayToFloatArray(audioData)
                
                val score = onnxModelRunner.analyzeVoice(floatData)
                resultScore.value = score
                statusText.value = "Analysis complete."
            } catch (e: Exception) {
                e.printStackTrace()
                statusText.value = "Error: ${e.message}"
            } finally {
                isAnalyzing.value = false
            }
        }
    }
}
