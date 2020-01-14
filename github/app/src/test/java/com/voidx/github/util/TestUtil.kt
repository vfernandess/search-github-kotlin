package com.voidx.github.util

import com.google.gson.Gson
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.nio.charset.StandardCharsets

object TestUtil {

    fun <T> getObject(fileName: String, classType: Class<T>): T {
        return Gson().fromJson(getJson(fileName), classType)
    }

    fun getJson(fileName: String): String {
        var json: String
        try {
            val classLoader = ClassLoader.getSystemClassLoader()
            val inputStream = classLoader.getResourceAsStream(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)

            inputStream.read(buffer)
            inputStream.close()

            json = String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            json = "{}"
        }

        return json
    }

    fun <T> createConnectionErrorObservable(): Observable<T> {
        return Observable.error<T>(createGenericError(500))
    }

    fun createGenericError(errorCode: Int): HttpException {

        var error = getJson("generic_error.json")
        error = String.format(error, errorCode.toString())

        val responseError = Response.error<Any>(
            errorCode,
            ResponseBody.create(MediaType.parse("application/json"), error)
        )

        return HttpException(responseError)
    }
}