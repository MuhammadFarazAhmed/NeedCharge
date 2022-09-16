package incubasys.needcharge.repositories.remote.client

import okhttp3.Interceptor
import okhttp3.Response

import java.io.IOException
import java.net.URI
import java.net.URISyntaxException

class ApiKeyAuth(private val location: String, private val paramName: String) : Interceptor {

    var apiKey: String? = null

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val paramValue: String
        var request = chain.request()

        if ("query" == location) {
            var newQuery: String? = request.url().uri().query
            paramValue = "$paramName=$apiKey"
            if (newQuery == null) {
                newQuery = paramValue
            } else {
                newQuery += "&$paramValue"
            }

            val newUri: URI
            try {
                newUri = URI(
                    request.url().uri().scheme, request.url().uri().authority,
                    request.url().uri().path, newQuery, request.url().uri().fragment
                )
            } catch (e: URISyntaxException) {
                throw IOException(e)
            }

            request = request.newBuilder().url(newUri.toURL()).build()
        } else if ("header" == location) {
            request = request.newBuilder()
                .addHeader(paramName, apiKey!!)
                .build()
        }
        return chain.proceed(request)
    }
}
