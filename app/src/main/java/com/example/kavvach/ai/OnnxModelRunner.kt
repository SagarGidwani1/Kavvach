package com.example.kavvach.ai

import android.content.Context
import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.nio.FloatBuffer

class OnnxModelRunner(private val context: Context) {

    private var env: OrtEnvironment? = null
    private var session: OrtSession? = null

    init {
        env = OrtEnvironment.getEnvironment()
        session = loadModel(context)
    }

    private fun loadModel(context: Context): OrtSession {
        val assetManager = context.assets
        val inputStream = assetManager.open("voice_detector.onnx")

        val modelFile = File(context.cacheDir, "voice_detector.onnx")
        inputStream.copyTo(FileOutputStream(modelFile))

        val env = OrtEnvironment.getEnvironment()
        return env.createSession(modelFile.absolutePath)
    }

    suspend fun analyzeVoice(audioFloatArray: FloatArray): Float = withContext(Dispatchers.Default) {
        val environment = env ?: throw IllegalStateException("ONNX Environment not initialized")
        val ortSession = session ?: throw IllegalStateException("ONNX Session not initialized")

        val shape = longArrayOf(1, audioFloatArray.size.toLong())
        val tensor = OnnxTensor.createTensor(
            environment,
            FloatBuffer.wrap(audioFloatArray),
            shape
        )

        val inputName = ortSession.inputNames.iterator().next()
        val inputs = mapOf(inputName to tensor)

        ortSession.run(inputs).use { results ->
            // Model outputs a probability score
            val output = results[0]?.value
            var score = 0f
            if (output is Array<*>) {
                val firstDim = output[0]
                if (firstDim is FloatArray) {
                    score = firstDim[0]
                }
            } else if (output is FloatArray) {
                score = output[0]
            }
            score
        }
    }
}
