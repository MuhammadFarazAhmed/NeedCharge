package incubasys.needcharge.repositories.utils

import com.google.gson.Gson
import incubasys.needcharge.datainterfaces.models.ErrorModel
import incubasys.needcharge.datainterfaces.models.Message
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


class ParseErrors @Inject
constructor(private val gson: Gson) {

    fun parseInternalErrors(throwable: Throwable): Message {
        val errorMessage = Message()
        throwable.printStackTrace()
        errorMessage.code = 900
        if (throwable is UnknownHostException) {
            errorMessage.code = 901
            errorMessage.message = "Unable to connect to internet"
        }
        if (throwable is ConnectException || throwable is SocketTimeoutException) {
            errorMessage.code = 902
            errorMessage.message = "Unable to connect, Please retry"
        }
        return errorMessage
    }

    fun parseServerErrors(statusCode: Int, body: String): Message {
        val errorObject = Message()
        errorObject.code = statusCode
        try {
            if (statusCode in 400..499) {
                if (body.isNotEmpty() && !body.contains("</html>")) {
                    val errorModel = gson.fromJson(body, ErrorModel::class.java)
                    errorModel.error?.let {
                        errorObject.message = it
                    }
                    errorModel.errors?.let { list ->
                        if (list.isNotEmpty()) {
                            errorObject.message = list[0]
                        } else {
                            errorObject.message = "No Error Found"
                        }
                    }
                    if (errorModel.code != 0)
                        errorObject.code = errorModel.code
                }
            } else if (statusCode in 500..599) {
                errorObject.message = "Server Error"
            }
        } catch (e: Exception) {
            //  Log.e("error", e.message, e)
            errorObject.message = body
        }

        return errorObject
    }
}
