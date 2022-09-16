package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.SettingsUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SettingsUsecaseImp @Inject constructor(
        private val authRepository: AuthRepository,
        private val preferencesHelper: PreferencesHelper,
        parseErrors: ParseErrors
) : BaseUsecase(parseErrors), SettingsUsecase {

    override fun isLoggedIn() = preferencesHelper.isLoggedIn

    override suspend fun signout(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.logout().await()
            }
            if (result.isSuccessful) {
                result.body()?.let {
                    preferencesHelper.removeHeaders()
                    preferencesHelper.removeUserInfo()
                }
                onLoading(false)
                onSuccess(result.isSuccessful)
            } else {
                onLoading(false)
                handleError(result, onError)
            }
        } catch (e: Exception) {
            onLoading(false)
            handleException(e, onError)
        }
    }


}