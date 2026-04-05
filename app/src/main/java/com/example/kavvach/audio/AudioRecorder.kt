package com.example.kavvach.audio

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AudioRecorder {
    // 16000 Hz * 5 seconds = 80000 samples
    private val sampleRate = 16000
    private val durationSeconds = 5
    private val totalSamples = sampleRate * durationSeconds

    @SuppressLint("MissingPermission")
    suspend fun recordAudio(): ShortArray = withContext(Dispatchers.IO) {
        val channelConfig = AudioFormat.CHANNEL_IN_MONO
        val audioFormat = AudioFormat.ENCODING_PCM_16BIT
        
        // Ensure buffer size is at least the minimum allowed
        val minBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
        val bufferSize = maxOf(minBufferSize, totalSamples * 2) // * 2 because 16-bit = 2 bytes

        val audioRecord = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize
        )

        val audioData = ShortArray(totalSamples)

        try {
            audioRecord.startRecording()
            
            var totalRead = 0
            while (totalRead < totalSamples) {
                val read = audioRecord.read(audioData, totalRead, totalSamples - totalRead)
                if (read < 0) {
                    throw Exception("Error reading audio data")
                }
                totalRead += read
            }
        } finally {
            audioRecord.stop()
            audioRecord.release()
        }

        audioData
    }
}
