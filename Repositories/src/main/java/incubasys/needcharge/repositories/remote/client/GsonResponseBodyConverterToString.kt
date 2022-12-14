package incubasys.needcharge.repositories.remote.client

import com.google.gson.Gson
import com.google.gson.JsonParseException
import okhttp3.ResponseBody
import retrofit2.Converter

import java.io.IOException
import java.lang.reflect.Type

/**
 * This wrapper is to take care of this case:
 * when the deserialization fails due to JsonParseException and the
 * expected type is String, then just return the body string.
 */
class GsonResponseBodyConverterToString<T> internal constructor(private val gson: Gson, private val type: Type) :
    Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        val returned = value.string()
        try {
            return gson.fromJson<T>(returned, type)
        } catch (e: JsonParseException) {
            @Suppress("UNCHECKED_CAST")
            return returned as T
        }

    }
}
