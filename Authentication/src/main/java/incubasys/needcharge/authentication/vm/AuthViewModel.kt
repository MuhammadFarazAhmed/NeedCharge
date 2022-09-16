package incubasys.needcharge.authentication.vm

import android.app.Application
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.datainterfaces.models.FacebookLoginInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.SupportContentOutput
import incubasys.needcharge.datainterfaces.usecases.AuthUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(application: Application, private val authUsecase: AuthUsecase) : BaseViewModel(application) {

    var content: SupportContentOutput? = null

    fun getSupportContent(onLoading: (Boolean) -> Unit, onSuccess: (SupportContentOutput) -> Unit, onError: (Message) -> Unit) {
        if (content == null) {
            viewModelScope.launch {
                authUsecase.getSupportContent(onLoading = onLoading,
                        onSuccess = {
                            content = it
                            onSuccess.invoke(it)
                        }, onError = onError)
            }
        } else {
            content?.let(onSuccess)
        }
    }

    fun loginWithFacebook(accessToken: String,onLoading : (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            authUsecase.loginWithFacebook(onLoading = onLoading,
                    onSuccess = onSuccess, onError = onError,
                    body = FacebookLoginInput(accessToken))
        }
    }
}