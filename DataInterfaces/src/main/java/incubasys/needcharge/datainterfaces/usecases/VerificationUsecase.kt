package incubasys.needcharge.datainterfaces.usecases

import incubasys.needcharge.datainterfaces.models.*

interface VerificationUsecase {

    suspend fun loginWithPhone(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: PhoneLoginInput
    )

    suspend fun signUpWithPhone(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: PhoneSignupInput
    )

    suspend fun changePhone(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: PhoneUpdate
    )

    suspend fun generatePin(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, phone: String, countryCode: String
    )
}