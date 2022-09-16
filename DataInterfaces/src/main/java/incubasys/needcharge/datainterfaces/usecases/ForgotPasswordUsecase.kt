package incubasys.needcharge.datainterfaces.usecases

import incubasys.needcharge.datainterfaces.models.ForgotPasswordInput
import incubasys.needcharge.datainterfaces.models.Message

interface ForgotPasswordUsecase {

    suspend fun forgotPassword(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: ForgotPasswordInput
    )
}
