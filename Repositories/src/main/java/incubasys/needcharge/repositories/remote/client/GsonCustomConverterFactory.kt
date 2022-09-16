package incubasys.needcharge.repositories.remote.client

import com.google.gson.Gson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class GsonCustomConverterFactory private constructor(private val gson: Gson?) : Converter.Factory() {
    private val gsonConverterFactory: GsonConverterFactory

    init {
        if (gson == null)
            throw NullPointerException("gson == null")
        this.gsonConverterFactory = GsonConverterFactory.create(gson)
    }

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *>? {
        return if (type == String::class.java)
            gson?.let { GsonResponseBodyConverterToString<Any>(it, type) }
        else
            gsonConverterFactory.responseBodyConverter(type!!, annotations!!, retrofit!!)
    }

    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<Annotation>?,
        methodAnnotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody>? {
        return gsonConverterFactory.requestBodyConverter(type!!, parameterAnnotations!!, methodAnnotations!!, retrofit!!)
    }

    companion object {

        fun create(gson: Gson): GsonCustomConverterFactory {
            return GsonCustomConverterFactory(gson)
        }
    }
}
