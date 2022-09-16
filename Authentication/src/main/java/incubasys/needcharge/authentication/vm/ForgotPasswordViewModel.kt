package incubasys.needcharge.authentication.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mlykotom.valifi.fields.ValiFieldEmail
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.datainterfaces.models.ForgotPasswordInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.usecases.ForgotPasswordUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor(application: Application, private val forgotPasswordUsecase: ForgotPasswordUsecase) : BaseViewModel(application) {
    val email = ValiFieldEmail()
    private val emailSent = MutableLiveData<Boolean>(false)
    private val error = MutableLiveData<Message>()
    fun getError(): LiveData<Message> = error

    init {
        email.addNotEmptyValidator()
    }

    fun sendEmail() {
        viewModelScope.launch {
            email.value?.let { email ->
                forgotPasswordUsecase.forgotPassword(onLoading = onLoading(), onSuccess = {
                    emailSent.value = it
                }, onError = onError(), body = ForgotPasswordInput(email))
            }
        }
    }

    fun getEmailSent(): LiveData<Boolean> = emailSent

    private fun onError(): (Message) -> Unit {
        return {
            error.value = it
        }
    }

}
