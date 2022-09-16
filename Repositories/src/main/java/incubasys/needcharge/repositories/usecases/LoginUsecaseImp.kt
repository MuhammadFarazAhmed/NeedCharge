package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.*
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.LoginUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginUsecaseImp @Inject constructor(
        private val authRepository: AuthRepository,
        private val preferencesHelper: PreferencesHelper,
        parseErrors: ParseErrors
) :
        AuthUsecaseImp(authRepository,preferencesHelper,  parseErrors), LoginUsecase  {

    override suspend fun loginWithEmail(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, loginInput: EmailLoginInput) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.loginWithEmail(loginInput).await()
            }
            if (result.isSuccessful) {
                result.body()?.let {
                    preferencesHelper.saveAuthHeaders(stoken = it.stoken)
                    it.user?.let { user -> preferencesHelper.saveUserInfo(user) }
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

    override suspend fun loginWithPhone(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, loginInput: PhoneLoginInput) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.loginWithPhone(loginInput).await()
            }
            if (result.isSuccessful) {
                result.body()?.let {
                    preferencesHelper.saveAuthHeaders(stoken = it.stoken)
                    it.user?.let { user -> preferencesHelper.saveUserInfo(user) }
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