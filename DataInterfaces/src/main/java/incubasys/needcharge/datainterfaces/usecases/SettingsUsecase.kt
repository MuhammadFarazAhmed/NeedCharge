package incubasys.needcharge.datainterfaces.usecases

import androidx.databinding.ObservableBoolean
import incubasys.needcharge.datainterfaces.models.Message

interface SettingsUsecase {

    fun isLoggedIn(): ObservableBoolean

    suspend fun signout(
            onLoading: (boolean: Boolean) -> Unit = {},
            onSuccess: (boolean: Boolean) -> Unit = {},
            onError: (message: Message) -> Unit = {}
    )

}