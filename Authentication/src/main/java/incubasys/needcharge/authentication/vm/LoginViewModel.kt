package incubasys.needcharge.authentication.vm

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mlykotom.valifi.ValiFiForm
import com.mlykotom.valifi.fields.ValiFieldEmail
import com.mlykotom.valifi.fields.ValiFieldPassword
import com.mlykotom.valifi.fields.ValiFieldText
import incubasys.needcharge.authentication.data.AuthType
import incubasys.needcharge.base.BaseViewModel
import incubasys.needcharge.base.utils.Utils.ifLet
import incubasys.needcharge.datainterfaces.models.EmailLoginInput
import incubasys.needcharge.datainterfaces.models.FacebookLoginInput
import incubasys.needcharge.datainterfaces.models.Message
import incubasys.needcharge.datainterfaces.usecases.LoginUsecase
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

class LoginViewModel @Inject constructor(application: Application, private val loginUsecase: LoginUsecase) : BaseViewModel(application) {
    lateinit var loginType: AuthType
    val email = ValiFieldEmail()
    val phone = ValiFieldText()
    val password = ValiFieldPassword()


    val form: ValiFiForm by lazy {
        when (loginType) {
            AuthType.EMAIL -> {
                ValiFiForm(email, password)
            }
            AuthType.PHONE -> {
                ValiFiForm(phone)
            }
        }
    }

    private val error = MutableLiveData<Message>()


    init {
        email.setEmptyAllowed(false)
        phone.setEmptyAllowed(false).addPatternValidator("Enter valid Phone", Pattern.compile("^\\+\\d{2} \\| \\d{3} \\d{4} \\d{3}\$"))
        password.setEmptyAllowed(false).addMinLengthValidator(8)
    }

    fun generatePinForLoginWithPhone(onLoading : (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            ifLet(phone.value) { (phoneNonNull) ->
                val simplePhone = phoneNonNull.replace("[+\\s|]".toRegex(),"")
                loginUsecase.generatePin(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        phone = simplePhone.drop(3), countryCode = simplePhone.dropLast(9))
            }
        }
    }

    fun loginWithFacebook(accessToken: String,onLoading : (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            loginUsecase.loginWithFacebook(onLoading = onLoading,
                    onSuccess = onSuccess, onError = onError,
                    body = FacebookLoginInput(accessToken))
        }
    }

    private fun onError(): (Message) -> Unit {
        return {
            error.value = it
        }
    }

    fun loginEmail(onLoading : (Boolean) -> Unit, onSuccess: (Boolean) -> Unit, onError: (Message) -> Unit) {
        viewModelScope.launch {
            ifLet(email.value, password.value) { (emailNonNull, passwordNonNull) ->
                loginUsecase.loginWithEmail(onLoading = onLoading,
                        onSuccess = onSuccess, onError = onError,
                        loginInput = EmailLoginInput(emailNonNull, passwordNonNull))
            }
        }
    }
}