package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.FacebookLoginInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.SupportContentOutput
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.AuthUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class AuthUsecaseImp @Inject constructor(
        private val authRepository: AuthRepository,
        private val preferencesHelper: PreferencesHelper,
        parseErrors: ParseErrors
) :
        BaseUsecase(parseErrors), AuthUsecase {


    override suspend fun generatePin(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, phone: String, countryCode: String) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.generatePin(phone, countryCode).await()
            }
            if (result.isSuccessful) {
                onLoading(false)
                onSuccess(true)
            } else {
                onLoading(false)
                handleError(result, onError)
            }
        } catch (e: Exception) {
            onLoading(false)
            handleException(e, onError)
        }
    }

    override suspend fun loginWithFacebook(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: FacebookLoginInput) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.signUpWithFacebook(body).await()
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

    override suspend fun getSupportContent(onLoading: (boolean: Boolean) -> Unit, onSuccess: (output: SupportContentOutput) -> Unit, onError: (message: Message) -> Unit) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.getSupportContent().await()
            }
            if (result.isSuccessful) {
                onLoading(false)
                result.body()?.let {
                    onSuccess(it)
                }
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