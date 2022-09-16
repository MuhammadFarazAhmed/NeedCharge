package incubasys.needcharge.datainterfaces.usecases

import incubasys.needcharge.datainterfaces.models.Message

interface SplashUsecase {

    suspend fun validateSession(
        onLoading: (boolean: Boolean) -> Unit = {},
        onSuccess: (boolean: Boolean) -> Unit = {},
        onError: (message: Message) -> Unit = {}
    )

    suspend fun logout(
        onLoading: (boolean: Boolean) -> Unit = {},
        onSuccess: (boolean: Boolean) -> Unit = {},
        onError: (message: Message) -> Unit = {}
    )

    fun showOnboardingScreen(): Boolean
}