package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.*
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.ProfileUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileUsecaseImp @Inject constructor(
        private val authRepository: AuthRepository,
        private val preferencesHelper: PreferencesHelper,
        parseErrors: ParseErrors
) : BaseUsecase(parseErrors), ProfileUsecase {


    override suspend fun getUser(onLoading: (boolean: Boolean) -> Unit, onSuccess: (user: User) -> Unit, onError: (message: Message) -> Unit) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.validateSession().await()
            }
            if (result.isSuccessful) {
                onLoading(false)
                result.body()?.let {
                    it.user?.let { user ->
                        preferencesHelper.saveUserInfo(user)
                        onSuccess(user)
                    }
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

    override suspend fun updateName(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: NameUpdate) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.updateName(body).await()
            }
            if (result.isSuccessful) {
                result.body()?.let {
                    preferencesHelper.saveUserInfo(it)
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

   /* override suspend fun updatePhone(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: PhoneUpdate) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                aauthRepository.updatePhone(body).await()
            }
            if (result.isSuccessful) {
               result.body()?.let {
                    preferencesHelper.saveUserInfo(it)
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

    }*/

    override suspend fun updateEmail(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: EmailUpdate) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.updateEmail(body).await()
            }
            if (result.isSuccessful) {
                result.body()?.let {
                    preferencesHelper.saveUserInfo(it)
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