package com.example.kavvach.utils

object AudioUtils {
    fun shortArrayToFloatArray(shortArray: ShortArray): FloatArray {
        val floatArray = FloatArray(shortArray.size)
        for (i in shortArray.indices) {
            floatArray[i] = shortArray[i] / 32768.0f
        }
        return floatArray
    }
}
