package incubasys.needcharge.datainterfaces.usecases

import incubasys.needcharge.datainterfaces.models.FacebookLoginInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.SupportContentOutput

interface AuthUsecase {

    suspend fun generatePin(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, phone: String, countryCode: String
    )

    suspend fun loginWithFacebook(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: FacebookLoginInput
    )

    suspend fun getSupportContent(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (output: SupportContentOutput) -> Unit = {},
            onError: (message: Message) -> Unit = {}
    )

}
