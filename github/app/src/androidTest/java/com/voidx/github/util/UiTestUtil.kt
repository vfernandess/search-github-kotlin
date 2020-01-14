package com.voidx.github.util

import android.util.Log
import androidx.test.InstrumentationRegistry
import java.io.IOException


object UiTestUtil {

    fun readFixture(file: String): String {
        val path = "fixtures/"
        return try {
            val openFile = InstrumentationRegistry.getContext().assets.open(path + file)
            val size = openFile.available()
            val buffer = ByteArray(size)
            openFile.read(buffer)
            openFile.close()
            String(buffer, Charsets.UTF_8)
        } catch (e: IOException) {
            Log.e("TEST", "readFixture: ", e)
            return ""
        }
    }

}