package incubasys.needcharge.datainterfaces.usecases

import incubasys.needcharge.datainterfaces.models.EmailLoginInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.PhoneLoginInput

interface LoginUsecase: AuthUsecase {

    suspend fun loginWithEmail(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, loginInput: EmailLoginInput
    )

    suspend fun loginWithPhone(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, loginInput: PhoneLoginInput
    )
}