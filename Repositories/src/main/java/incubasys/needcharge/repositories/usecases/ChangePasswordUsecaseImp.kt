package incubasys.needcharge.repositories.usecases

import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.Password
import incubasys.needcharge.datainterfaces.repository.AuthRepository
import incubasys.needcharge.datainterfaces.usecases.ChangePasswordUsecase
import incubasys.needcharge.repositories.utils.ParseErrors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ChangePasswordUsecaseImp @Inject constructor(
        private val authRepository: AuthRepository,
        parseErrors: ParseErrors
) : BaseUsecase(parseErrors), ChangePasswordUsecase {

    override suspend fun changePassword(onLoading: (boolean: Boolean) -> Unit, onSuccess: (boolean: Message) -> Unit, onError: (message: Message) -> Unit, body: Password) {
        onLoading(true)
        try {
            val result = withContext(Dispatchers.IO) {
                authRepository.changePassword(body).await()
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