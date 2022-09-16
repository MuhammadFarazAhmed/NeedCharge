package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.PhoneLoginInput
import incubasys.needcharge.datainterfaces.models.PhoneSignupInput
import incubasys.needcharge.datainterfaces.models.PhoneUpdate
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.VerificationUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VerificationUsecaseImp @Inject constructor(
        private val authRepository: AuthRepository,
        private val preferencesHelper: PreferencesHelper,
        parseErrors: ParseErrors
) : BaseUsecase(parseErrors), VerificationUsecase {


    override suspend fun loginWithPhone(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: PhoneLoginInput) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.loginWithPhone(body).await()
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

    override suspend fun signUpWithPhone(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: PhoneSignupInput) {
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

    override suspend fun changePhone(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: PhoneUpdate) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.updatePhone(body).await()
            }
            if (result.isSuccessful) {
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

    override suspend fun generatePin(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, phone: String, countryCode: String) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.generatePin(phone, countryCode).await()
            }
            if (result.isSuccessful) {
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