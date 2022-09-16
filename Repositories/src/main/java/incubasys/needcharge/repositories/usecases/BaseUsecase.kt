package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.repositories.utils.ParseErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

open class BaseUsecase(private val parseErrors: ParseErrors) {


    suspend fun handleError(
            result: Response<*>,
            onError: (message: Message) -> Unit
    ) {
        var error = ""
        result.errorBody()?.string()?.let {
            error = if (it.isNotEmpty()) {
                it
            } else {
                result.message()
            }
        }
        if (error.isEmpty()) error = result.message()
        handleError(result.code(), error, onError)
    }

    suspend fun handleException(
            e: Exception,
            onError: (message: Message) -> Unit
    ) {
        val error = withContext(Dispatchers.IO) {
            parseErrors.parseInternalErrors(e)
        }
        println("onError:  ${Thread.currentThread()}")
        onError(error)
    }

    private suspend fun handleError(
            code: Int, errorBody: String,
            onError: (message: Message) -> Unit
    ) {
        val error = withContext(Dispatchers.IO) {
            println("creating error method:  ${Thread.currentThread()}")
            parseErrors.parseServerErrors(code, errorBody)
        }
        println("onError:  ${Thread.currentThread()}")
        onError(error)
    }
}