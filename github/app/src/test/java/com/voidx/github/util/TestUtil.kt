package com.voidx.github.util

import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.nio.charset.StandardCharsets

object TestUtil {

    fun <T> getObject(fileName: String, classType: Class<T>): T {
        val json = getJson(fileName)
        return Gson().fromJson(json, classType)
    }

    fun getJson(fileName: String): String {
        return try {
            val classLoader = ClassLoader.getSystemClassLoader()
            val inputStream = classLoader.getResourceAsStream(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()

            String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            "{}"
        }
    }

    fun <T> createConnectionErrorObservable(): Observable<T> {
        return Observable.error<T>(createGenericError(500))
    }

    fun createGenericError(errorCode: Int): HttpException {

        var error = getJson("generic_error.json")
        error = String.format(error, errorCode.toString())

        val responseError = Response.error<Any>(
            errorCode,
            error.toResponseBody("application/json".toMediaTypeOrNull())
        )

        return HttpException(responseError)
    }
}