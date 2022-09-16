package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.SplashUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import incubasys.needcharge.repositories.utils.PreferencesHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SplashUsecaseImp @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesHelper: PreferencesHelper,
    parseErrors: ParseErrors
) :
    BaseUsecase(parseErrors), SplashUsecase {


    override suspend fun validateSession(
        onLoading: (boolean: Boolean) -> Unit,
        onSuccess: (boolean: Boolean) -> Unit,
        onError: (message: Message) -> Unit
    ) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.validateSession().await()
            }
            if (result.isSuccessful) {
                val success = withContext(Dispatchers.IO) {
                    println("creating success method:  ${Thread.currentThread()}")
                    // TODO Save Info
                    // preferencesHelper.saveAuthHeaders()
                    result.isSuccessful
                }
                println("onSuccess:  ${Thread.currentThread()}")
                onLoading(false)
                onSuccess(success)
            } else {
                onLoading(false)
                handleError(result, onError)
            }
        } catch (e: Exception) {
            onLoading(false)
            handleException(e, onError)
        }
    }

    override suspend fun logout(
        onLoading: (boolean: Boolean) -> Unit,
        onSuccess: (boolean: Boolean) -> Unit,
        onError: (message: Message) -> Unit
    ) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.logout().await()
            }
            if (result.isSuccessful) {
                val success = withContext(Dispatchers.IO) {
                    println("creating success method:  ${Thread.currentThread()}")
                    preferencesHelper.removeHeaders()
                    preferencesHelper.removeUserInfo()
                    result.isSuccessful
                }
                println("onSuccess:  ${Thread.currentThread()}")
                onLoading(false)
                onSuccess(success)
            } else {
                onLoading(false)
                handleError(result, onError)
            }
        } catch (e: Exception) {
            onLoading(false)
            handleException(e, onError)
        }
    }

    override fun showOnboardingScreen() = preferencesHelper.isOnboardingShown


}