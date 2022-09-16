package incubasys.needcharge.datainterfaces.usecases

import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.Password

interface ChangePasswordUsecase {

    suspend fun changePassword(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Message) -> Unit = {},
            onError: (message: Message) -> Unit = {}, body: Password
    )
}
