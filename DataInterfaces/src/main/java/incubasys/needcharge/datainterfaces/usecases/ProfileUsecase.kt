package incubasys.needcharge.datainterfaces.usecases

import incubasys.needcharge.datainterfaces.models.*

interface ProfileUsecase {

    suspend fun getUser(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (user: User) -> Unit = {},
            onError: (message: Message) -> Unit = {}
    )

    suspend fun updateName(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: NameUpdate
    )

  /*  suspend fun updatePhone(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: PhoneUpdate
    )*/

    suspend fun updateEmail(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: EmailUpdate
    )

    suspend fun generatePin(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}, phone: String, countryCode: String
    )

}