package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.EmailSignupInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.PhoneSignupInput
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.SignupUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignupUsecaseImp @Inject constructor(
        private val authRepository: AuthRepository,
        private val preferencesHelper: PreferencesHelper,
        parseErrors: ParseErrors
) :
        AuthUsecaseImp(authRepository, preferencesHelper, parseErrors), SignupUsecase {

    override suspend fun signupWithEmail(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: EmailSignupInput) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.signUpWithEmail(body).await()
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

    override suspend fun signupWithPhone(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: PhoneSignupInput) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.signUpWithPhone(body).await()
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