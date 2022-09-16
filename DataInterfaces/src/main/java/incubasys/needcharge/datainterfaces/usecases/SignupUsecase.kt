package incubasys.needcharge.datainterfaces.usecases

import incubasys.needcharge.datainterfaces.models.EmailSignupInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.PhoneSignupInput

interface SignupUsecase : AuthUsecase {

    suspend fun signupWithEmail(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: EmailSignupInput
    )

    suspend fun signupWithPhone(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: PhoneSignupInput
    )

}