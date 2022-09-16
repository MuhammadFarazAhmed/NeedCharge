package incubasys.needcharge.authentication.vm

import android.app.Application
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldPassword
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.base.utils.Utils
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.models.Password
import incubasys.needcharge.datainterfaces.usecases.ChangePasswordUsecase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(application: Application, private val changePasswordUsecase: ChangePasswordUsecase) : BaseViewModel(application) {


    val password = ValiFieldPassword()
    val confimPassword = ValiFieldPassword()

    val form = ValiFiForm(password, confimPassword)

    init {
        password.setEmptyAllowed(false).addMinLengthValidator(8)
        confimPassword.addVerifyFieldValidator("Passwords must be the same", password)
    }


    fun changePassword(onLoading: (Boolean) -> Unit, onSuccess: (Message) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            Utils.ifLet(password.value, confimPassword.value) { (passwordNonNull, confirmPasswordNonNull) ->
                changePasswordUsecase.changePassword(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        body = Password(password = passwordNonNull, passwordConfirmation = confirmPasswordNonNull))
            }
        }
    }


}
