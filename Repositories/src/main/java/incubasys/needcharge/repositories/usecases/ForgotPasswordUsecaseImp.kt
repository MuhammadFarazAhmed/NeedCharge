package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.ForgotPasswordInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.ForgotPasswordUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ForgotPasswordUsecaseImp @Inject constructor(
        private val authRepository: AuthRepository,
        parseErrors: ParseErrors
) :
        BaseUsecase(parseErrors), ForgotPasswordUsecase {
    override suspend fun forgotPassword(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Boolean) -> Unit, onError: (message: Message) -> Unit, body: ForgotPasswordInput) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.forgotPassword(body).await()
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


}